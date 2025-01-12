package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.util.MethodUtil;

/**
 * traverse and set method name, class name
 */
public class SetNodeMethodNameVisitor extends AbstractVisitor {

    @Override
    public boolean preVisit(AbstractNode node) {
        return true;
    }

    @Override
    public boolean postVisit(AbstractNode node) {

        return true;
    }

    @Override
    public boolean visit(AbstractNode node) {
        if (node.isVisited) {
            return false;
        }
        node.isVisited = true;
        Node n = (Node) node;

        String s = n.getMethod().getDeclaringClass().getName();
        n.setDeclaringClass(s);
        String methodSig = MethodUtil.getShortMethodSignatureName(n.getMethod());
        n.setMethodSignatureParamShort(methodSig);

        return true;
    }

    @Override
    public boolean endVisit(AbstractNode node) {
        return true;
    }


    @Override
    public boolean isPhantom(AbstractNode node) {
        return false;
    }

}
