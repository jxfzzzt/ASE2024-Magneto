package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;

/**
 * traverse from root and collect method name and class name
 * put into classDataContainer
 */
public class CollectMethodNameVisitor extends AbstractVisitor {

    public ClassDataContainer classDataContainer = new ClassDataContainer();


    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {

        return true;
    }


    public boolean visit(AbstractNode node) {
        Node n = (Node) node;
        classDataContainer.addEntry(n.getDeclaringClass(), n.getMethodSignatureParamShort());
        return true;

    }

    public boolean endVisit(AbstractNode node) {
        return true;
    }


}
