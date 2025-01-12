package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;

import java.util.Map;

public class SetMapVisitor extends AbstractVisitor {

    public Map<String, Node> innerMap;

    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {
        return true;
    }


    public boolean visit(AbstractNode node) {
        Node n = (Node) node;
        String methodSig = n.getMethodSignatureFull();
        innerMap.put(methodSig, n);
        return true;

    }

    public boolean endVisit(AbstractNode node) {
        return true;
    }
}
