package com.magneto.staticanalysis.callgraph;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.magneto.config.ClientProjectProperty;
import com.magneto.config.GlobalConfiguration;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.staticanalysis.callgraph.analysis.vis.InitGraphNodeVisitor;
import com.magneto.staticanalysis.callgraph.analysis.vis.SetNodeMethodNameVisitor;
import com.magneto.staticanalysis.callgraph.global.CallGraphGlobal;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import soot.*;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
public class ProjectCallGraph {

    private static final String RT_JAR_NAME = "rt.jar";

    private String rtJarPath;

    private final String projectJarPath;

    private final List<String> dependencyJarPaths;

    private CallGraphBean callGraphBean;

    public ProjectCallGraph(@NonNull String projectJarPath, @NonNull List<String> dependencyJarPaths) throws IOException, ClassNotFoundException {
        this.projectJarPath = projectJarPath;
        this.dependencyJarPaths = dependencyJarPaths;

        generateRTJar();
        preProcess();

        this.callGraphBean = buildCallGraphBean();
        this.callGraphBean = analysisCallGraphBean(callGraphBean, callGraphBean.getCiaMethod());
        this.callGraphBean = handleInterfaceMethod(callGraphBean);

        assertNotNull(callGraphBean);
    }

    private void generateRTJar() throws IOException {
        InputStream stream = new ClassPathResource(RT_JAR_NAME).getStream();

        OutputStream outputStream = null;

        this.rtJarPath = GlobalConfiguration.CACHE_DIR_PATH + File.separator + "rt.jar";
        outputStream = Files.newOutputStream(Paths.get(this.rtJarPath));

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        stream.close();

        if (outputStream != null) {
            outputStream.close();
        }

        assertTrue(FileUtil.exist(rtJarPath));
    }

    // Soot config
    private void preProcess() {
        List<String> jarPaths = new ArrayList<>();
        jarPaths.add(this.projectJarPath);
        jarPaths.addAll(this.dependencyJarPaths);

        G.reset();
        List<String> argsList = new ArrayList<>();

        argsList.addAll(Arrays.asList(new String[]{
                "-allow-phantom-refs",
                "-w",
                "-keep-line-number", "enabled",
                // "-no-bodies-for-excluded",
                "-cp", this.rtJarPath
        }));

        for (String s : jarPaths) {
            argsList.add("-process-dir");
            argsList.add(s);
        }

        argsList.addAll(Arrays.asList(new String[]{"-p", "jb", "use-original-names:true"}));

        String[] args = argsList.toArray(new String[0]);
        Options.v().parse(args);
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_keep_line_number(true);
        Options.v().set_verbose(true);
        Options.v().set_keep_line_number(true);
        Options.v().setPhaseOption("cg", "all-reachable:true");
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_app(true);
        Options.v().set_num_threads(15);
    }


    // init call graph bean
    private CallGraphBean buildCallGraphBean() {
        Scene.v().setEntryPoints(ClientProjectProperty.getEntryMethods());

        Scene.v().loadNecessaryClasses();

//        Scene.v().loadBasicClasses();

        CHATransformer.v().transform();

        CallGraph callGraph = Scene.v().getCallGraph();
        CallGraphBean callGraphBean = new CallGraphBean(callGraph);
        for (SootMethod sootMethod : ClientProjectProperty.getEntryMethods()) {
            Node newNode = Node.createNodeInstance(sootMethod);
            if (newNode != null) {
                callGraphBean.addCiaMethod(newNode);
            }
        }
        return callGraphBean;
    }

    // analysis call graph bean
    private CallGraphBean analysisCallGraphBean(CallGraphBean callGraphBean, List<Node> roots) {
        //analysisFlow.runGraph
        for (Node method : roots) {
            method.acceptBFS(new InitGraphNodeVisitor(callGraphBean.getCallGraph()));
        }

        SetNodeMethodNameVisitor setNodeMethodNameVisitor = new SetNodeMethodNameVisitor();
        for (Node n : roots) {
            n.accept(setNodeMethodNameVisitor);
        }
        for (Node n : CallGraphGlobal.nodeSet) {
            n.isVisited = false;
        }
        return callGraphBean;
    }

    // handle interface
    private CallGraphBean handleInterfaceMethod(CallGraphBean callGraphBean) {
        FastHierarchy fh = Scene.v().getOrMakeFastHierarchy();
        // get all root method which is interface function
        for (Node root : callGraphBean.getCiaMethod()) {
            Map<SootClass, List<SootMethod>> interfaceMethod = new HashMap<>();

            SootMethod sootMethod = root.getMethod();

            SootClass sootClass = sootMethod.getDeclaringClass();
            if (sootClass.isInterface()) {
                if (!interfaceMethod.containsKey(sootClass)) {
                    interfaceMethod.put(sootClass, new ArrayList<>());
                }
                interfaceMethod.get(sootClass).add(sootMethod);
            }

            List<Node> roots = new ArrayList<>();
            Iterator var1 = Scene.v().getClasses().getElementsUnsorted().iterator();
            while (var1.hasNext()) {
                SootClass sc = (SootClass) var1.next();
                if (interfaceMethod.containsKey(sc)) {
                    List<SootMethod> methods = interfaceMethod.get(sc);
                    for (SootMethod method : methods) {
                        Set<SootMethod> implementMethods = fh.resolveAbstractDispatch(sc, method);
                        for (SootMethod implementMethod : implementMethods) {
                            Node child = Node.createNodeInstance(implementMethod);
                            root.getChildren().add(child);
                            roots.add(child);
                        }
                    }
                }
            }
            analysisCallGraphBean(callGraphBean, roots);
        }


        return callGraphBean;
    }

    public CallGraphBean getCallGraphBean() {
        return callGraphBean;
    }

}


