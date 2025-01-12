package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.*;

import java.util.ArrayList;
import java.util.List;

public class AnalysisDataFlowVisitor extends AbstractVisitor {
    @Override
    public boolean preVisit(AbstractNode node) {
        return true;
    }

    @Override
    public boolean postVisit(AbstractNode node) {
//        Node n = (Node)node;
//        if(n.getMethod().isPhantom()){
//            return true;
//        }
        return true;
    }

    @Override
    public boolean visit(AbstractNode node) {
        List<UnitWrapper> paramUnitWrappers = DataFlowParse.getParamUnit((Node) node);
        VarChain varChain = new VarChain();
        List<Var> doChangedVars = new ArrayList<>();
        for (UnitWrapper paramUnit : paramUnitWrappers) {
            ParameterStmt pa = (ParameterStmt) paramUnit.parsedStmt;
            varChain.addAnchor(pa.leftVar);
            DataFlowParse.beginFlow((Node) node, paramUnit, paramUnit.successorUnitWrapper, varChain, doChangedVars);
        }
        List<Var> changedParams = varChain.traceChangedVarBackwardsToFindChangedParams(doChangedVars);
        //todo set method IO
        if (node.getChildren() != null && node.getChildren().size() != 0) {
            return true;
        }
        return true;
    }

    @Override
    public boolean endVisit(AbstractNode node) {
        return false;
    }
}
