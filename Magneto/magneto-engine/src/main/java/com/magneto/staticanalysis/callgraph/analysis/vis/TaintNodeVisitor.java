package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractNode;
import com.magneto.staticanalysis.callgraph.analysis.bean.AbstractVisitor;
import com.magneto.staticanalysis.callgraph.analysis.bean.Node;
import com.magneto.staticanalysis.callgraph.analysis.bean.UnitWrapper;
import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class TaintNodeVisitor extends AbstractVisitor {

    private final ClassDataContainer container;
    /**
     * prev->curr curr->prev
     */
    private final Map<String, Node> counterNodeMap;

    public TaintNodeVisitor(ClassDataContainer container, Map<String, Node> counterMap) {
        this.container = container;
        this.counterNodeMap = counterMap;
    }

    public boolean preVisit(AbstractNode node) {
        return true;
    }

    public boolean postVisit(AbstractNode node) {
        return true;
    }


    public boolean visit(AbstractNode node) {
        Node n = (Node) node;
        List<UnitWrapper> wrappers = n.getUnitWrapperContainer().getUnitWrappers();
        n.getUnitWrapperContainer().initRange();
        MethodData matchedMethodData = isTaintable(container, n.getDeclaringClass(), n.getMethodSignatureParamShort());
        if (matchedMethodData.isAddedOrDeleteOnGraph) {
            n.isChange = CallGraphConstant.ADD_OR_DELETE;
        }
        boolean isChange = false;
        for (UnitWrapper uw : wrappers) {
            if (matchedMethodData.taintedLines.contains(uw.lineNumber)) {
                uw.isChange = true;
                isChange = true;
            }
        }
        if (isChange) {
            n.isChange = CallGraphConstant.CHANGE;
            if (counterNodeMap.containsKey(n.getMethodSignatureFull())) {
                Node counterpartNode = counterNodeMap.get(n.getMethodSignatureFull());
                counterpartNode.isChange = CallGraphConstant.CHANGE;
            } else {
                log.error("CornerCondition");
            }
        }
        return true;

    }

    public MethodData isTaintable(ClassDataContainer container, String className, String methodName) {
        MethodData methodData = container.classDataMap.get(className).methodDataMap.get(methodName);
        return methodData;
    }

    public boolean endVisit(AbstractNode node) {
        return true;
    }

}
