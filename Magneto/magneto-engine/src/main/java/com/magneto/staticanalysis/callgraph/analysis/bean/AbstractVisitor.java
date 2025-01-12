package com.magneto.staticanalysis.callgraph.analysis.bean;

import com.magneto.staticanalysis.callgraph.global.CallGraphGlobal;

public abstract class AbstractVisitor {
    private boolean getMethodFlag = true;

    public AbstractVisitor() {
        // Reset isVisited
        for (Node n : CallGraphGlobal.nodeSet) {
            n.isVisited = false;
        }
    }

    public AbstractVisitor(boolean flag) {
        this.getMethodFlag = flag;
    }

    /**
     * Pre-order traversal
     *
     * @param node
     * @return
     */
    public abstract boolean preVisit(AbstractNode node);

    /**
     * Post-order traversal
     *
     * @param node
     * @return
     */
    public abstract boolean postVisit(AbstractNode node);

    public abstract boolean visit(AbstractNode node);

    public abstract boolean endVisit(AbstractNode node);

    public boolean isPhantom(AbstractNode n) {
        if (n instanceof Node) {
            Node node = (Node) n;
            if (!this.getMethodFlag) {
                return false;
            }
            if (node.getMethod() != null) {
                return node.getMethod().isPhantom();
            } else {
                return true;
            }
        }
        return false;
    }


}
