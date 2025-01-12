package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;

public class ResetVisitFlagVisitor extends AbstractVisitor {

    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {
        return true;
    }


    public boolean visit(AbstractNode node) {
        node.isVisited = false;
        return true;
    }

    public boolean endVisit(AbstractNode node) {
        return true;
    }

}
