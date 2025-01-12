package com.magneto.staticanalysis.callgraph.analysis.vis;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.staticanalysis.callgraph.analysis.bean.UnitWrapper;
import com.magneto.staticanalysis.callgraph.analysis.dataflow.InvokeStmt;
import com.magneto.staticanalysis.callgraph.analysis.init.UnitParser;
import com.magneto.staticanalysis.callgraph.global.CallGraphConfig;
import com.magneto.staticanalysis.callgraph.global.CallGraphGlobal;
import com.magneto.util.IOUtil;
import lombok.extern.slf4j.Slf4j;
import soot.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * generate graph
 */
@Slf4j
public class InitGraphNodeVisitor extends AbstractVisitor {

    private final CallGraph callGraph;
    private final Set<String> jdkClasses;
    private final Set<String> used;

    public InitGraphNodeVisitor(CallGraph callGraph) {
        this.callGraph = callGraph;
        this.jdkClasses = new HashSet<>();
        this.used = new HashSet<>();
    }

    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {
        return true;
    }

    public boolean visit(AbstractNode node) {
        // BFS
        Node n = (Node) node;
        if (CallGraphGlobal.nodeSet.contains(n)) {
            return true;
        }
        CallGraphGlobal.nodeSet.add(n);
        initNode(n);
        UnitParser.runParseUnit(n);
//        List<Unit> li = new ArrayList<>();
        if (n.getUnitWrapperContainer() != null) {
            List<UnitWrapper> uws = n.getUnitWrapperContainer().getUnitWrappers();
            Map<UnitWrapper, List<SootMethod>> unitToNodePair = new HashMap<>();
            for (UnitWrapper uw : uws) {
                if (uw.parsedStmt instanceof InvokeStmt) {
                    if (isValidUnit(uw)) {
                        Iterator<Edge> edges = callGraph.edgesOutOf(uw.unit);
                        List<SootMethod> value = new ArrayList<>();
                        while (edges.hasNext()) {
                            Edge e = edges.next();
                            SootMethod sootMethod = (SootMethod) e.getTgt();
                            SootClass sootClass = sootMethod.getDeclaringClass();
                            if (isValidCallGraphNode(sootClass) && isValidMethod(sootMethod)) {
                                value.add(sootMethod);
                            }
                        }
                        if (value.size() == 0) {
                            //phantom node
                            InvokeStmt stmt = (InvokeStmt) uw.parsedStmt;
                            SootMethod sootMethod2 = stmt.sootMethod;
                            if (sootMethod2 != null) {
                                value.add(sootMethod2);
                            }
                        }
                        unitToNodePair.put(uw, value);
                    }
                }
//                li.add(uw.unit);
            }
            UnitParser.runMapUnitToNode(n, unitToNodePair);
        }
        return true;
    }

    public boolean endVisit(AbstractNode node) {
        return true;
    }

    /**
     * remove jdk nodes
     *
     * @param uw
     * @return
     */
    public boolean isValidUnit(UnitWrapper uw) {
        // cut jdk method
        InvokeStmt invokeStmt = (InvokeStmt) uw.parsedStmt;
        if (invokeStmt.invokeType == InvokeStmt.CLASS_NEW) {
            return false;
        }
        SootMethod sootMethod = invokeStmt.sootMethod;
        SootClass sootClass = sootMethod.getDeclaringClass();
        return isValidCallGraphNode(sootClass);
    }

    public boolean isValidCallGraphNode(SootClass sootClass) {
        String name = sootClass.getName();
        if (name.startsWith("java.")) {
            return false;
        }
        if (name.startsWith("javax.")) {
            return false;
        }
        if (name.startsWith("org.xml")) {
            return false;
        }
        if (name.startsWith("org.ietf")) {
            return false;
        }
        if (name.startsWith("org.omg")) {
            return false;
        }
        if (name.startsWith("org.w3c")) {
            return false;
        }
        return !isJDKMethod(name);

    }

    public boolean isJDKMethod(String className) {
        if (jdkClasses.size() == 0) {
            JSONArray jsonArray = null;
            try {
                InputStream JAVA_DOC_INPUTSTREAM = new ClassPathResource(CallGraphConfig.JAVA_DOC_NAME).getStream();
                String s = IOUtil.readString(JAVA_DOC_INPUTSTREAM);
                jsonArray = JSONUtil.parseArray(s);
            } catch (IOException e) {
                log.error("JavaDocFileReadError: ", e);
                throw new RuntimeException(e);
            }

            Iterator iter = jsonArray.iterator();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                jdkClasses.add(name);
            }
        }

        String fileName = "/docs/api/" + className + ".html";
        if (used.contains(fileName)) {
            return true;
        } else if (jdkClasses.contains(fileName)) {
            used.add(fileName);
            return true;
        }
        return false;
    }

    public boolean isValidMethod(SootMethod sootMethod) {
        String name = sootMethod.getName();
        //||"<init>".equals(name)
        return !"<clinit>".equals(name);
    }

    public void initNode(Node node) {
        Node n = node;
        if (n.getMethod().isPhantom()) {
            return;
        }

        try {
            if (n.getUnitWrapperContainer() == null) {
                n.initUnitWrapperContainer(0);
                Body body = n.getMethod().retrieveActiveBody();

                UnitGraph ug = new BriefUnitGraph(body);
                n.setUnitGraph(ug);
                UnitPatchingChain upc = body.getUnits();
                Iterator iter = upc.iterator();
                int i = 0;
                while (iter.hasNext()) {
                    Unit unit = (Unit) iter.next();
                    n.getUnitWrapperContainer().addUnitToUnitWrapper(i, unit);
                    i++;
                }
            }
        } catch (RuntimeException re) {
//            log.warn("a soot method body retrieval warning");
        }
    }


}
