package com.magneto.staticanalysis.callgraph;

import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import lombok.extern.slf4j.Slf4j;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CallGraphBean {

    private List<Node> root;
    private final List<Node> ciaMethod;
    private CallGraph callGraph;
    private Map<Node, Map<String, Node>> ciaMethodMapToMethodStringNode;

    private final Map<AbstractNode, List<AbstractNode>> callingEdges;
    private final Map<AbstractNode, List<AbstractNode>> calledEdges;


    public CallGraphBean(CallGraph callGraph) {
        callingEdges = new HashMap<>();
        calledEdges = new HashMap<>();
        ciaMethod = new ArrayList<>();
        root = new ArrayList<>();
        this.callGraph = callGraph;
    }

    public List<Node> getRoot() {
        return root;
    }

    /**
     * set once to
     */
    public void setRoot() {
        if (this.root.size() != 0) {
            return;
        }
        this.root = root;
    }

    public List<Node> getCiaMethod() {
        return ciaMethod;
    }

    public List<SootMethod> getCiaSootMethod() {
        List<SootMethod> result = new ArrayList<>();
        for (Node n : this.ciaMethod) {
            result.add(n.getMethod());
        }
        return result;
    }

    public List<AbstractNode> getCalleeFromCaller(SootMethod caller) {
        if (this.callingEdges.containsKey(caller)) {
            return this.callingEdges.get(caller);
        }
        return null;
    }

    public List<AbstractNode> getCallerFromCallee(SootMethod callee) {
        if (this.calledEdges.containsKey(callee)) {
            return this.calledEdges.get(callee);
        }
        return null;
    }


    public void addCiaMethod(Node ciaMethod) {
        this.ciaMethod.add(ciaMethod);
    }

    public Map<AbstractNode, List<AbstractNode>> getCallingEdges() {
        return callingEdges;
    }

    public Map<SootMethod, List<SootMethod>> getCallingSootMethods() {
        Map<SootMethod, List<SootMethod>> result = new HashMap<>();
        for (Map.Entry<AbstractNode, List<AbstractNode>> entry : this.callingEdges.entrySet()) {
            List<SootMethod> sub = new ArrayList<>();
            for (AbstractNode an : entry.getValue()) {
                Node n = (Node) an;
                sub.add(n.getMethod());
            }
            Node n2 = (Node) entry.getKey();
            result.put(n2.getMethod(), sub);
        }
        return result;
    }

    public Map<AbstractNode, List<AbstractNode>> getCalledEdges() {
        return calledEdges;
    }

    public void addCallingEdges(Node key, List<AbstractNode> value) {
        if (this.callingEdges.containsKey(key)) {
            List<AbstractNode> mList = this.callingEdges.get(key);
            mList.addAll(value);
        } else {
            this.callingEdges.put(key, value);
        }
    }

    public void addCalledEdges(Node key, List<AbstractNode> value) {
        if (this.calledEdges.containsKey(key)) {
            List<AbstractNode> mList = this.calledEdges.get(key);
            mList.addAll(value);
        } else {
            this.calledEdges.put(key, value);
        }

    }

    public CallGraph getCallGraph() {
        return callGraph;
    }

    public void setCallGraph(CallGraph callGraph) {
        this.callGraph = callGraph;
    }

    public Map<Node, Map<String, Node>> getCiaMethodMapToMethodStringNode() {
        return ciaMethodMapToMethodStringNode;
    }

    public void setCiaMethodMapToMethodStringNode(Map<Node, Map<String, Node>> ciaMethodMapToMethodStringNode) {
        this.ciaMethodMapToMethodStringNode = ciaMethodMapToMethodStringNode;
    }
}
