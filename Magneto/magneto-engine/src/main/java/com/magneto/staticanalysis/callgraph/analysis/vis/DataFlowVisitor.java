package com.magneto.staticanalysis.callgraph.analysis.vis;

import com.magneto.staticanalysis.callgraph.analysis.bean.*;
import com.magneto.staticanalysis.callgraph.analysis.dataflow.*;
import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataFlowVisitor extends AbstractVisitor {

    public ClassDataContainer container;

    @Override
    public boolean preVisit(AbstractNode node) {

        return true;
    }

    @Override
    public boolean postVisit(AbstractNode node) {
        Node n = (Node) node;
        if (n.getMethod().isPhantom()) {
            return true;
        }
        // add ore delete
        String methodName = n.getMethodSignatureParamShort();
        String className = n.getDeclaringClass();
        if (container.classDataMap.get(className).methodDataMap.get(methodName).isAddedOrDeleteOnGraph) {
            n.isChange = CallGraphConstant.CHANGE;
            return true;
        }
        if (n.getMethod().isPhantom()) {
            return true;
        }
        List<UnitWrapper> wrappers = n.getUnitWrapperContainer().getUnitWrappers();
        boolean isChange = false;
        for (UnitWrapper uw : wrappers) {
            if (uw.isChange) {
                isChange = true;
                break;
            }
        }
        if (!isChange) {
            return true;
        }
        doDataFlowAnalysis(n);

        return true;
    }

    @Override
    public boolean visit(AbstractNode node) {
        return true;
    }

    @Override
    public boolean endVisit(AbstractNode node) {
        return false;
    }

    public void doDataFlowAnalysis(Node node) {
        List<UnitWrapper> wrappers = node.getUnitWrapperContainer().getUnitWrappers();
        wrappers.forEach(a -> a.isVisited = false);
        List<UnitWrapper> paramUnitWrappers = DataFlowParse.getParamUnit(node);
        // if varchain have var
        List<Var> paramVars = new ArrayList<>();
        for (UnitWrapper uw : paramUnitWrappers) {
            ParameterStmt parameterStmt = (ParameterStmt) uw.parsedStmt;
            paramVars.add(parameterStmt.leftVar);
        }

//        node.getUnitToNodePairList();
        List<UnitWrapperToNodePair> unitWrapperToNodePair = node.getUnitToNodePairList();
        for (UnitWrapperToNodePair pair : unitWrapperToNodePair) {
            Node children = pair.getToInvokedMethod();
            NodeIOVar nodeIOVar = children.getNodeIOVars();

            UnitWrapper from = pair.getFromInvokeStmt();
            List<VarPair> varPairs = pair.getBaseAndArgsPair();
            boolean flag = false;
            for (VarPair varPair : varPairs) {
                if (varPair.to.isChange) {
                    varPair.from.isChange = true;
                    flag = true;
                }
            }
            List<VarPair> retPairs = pair.getReturnValuePair();
            for (VarPair varPair : retPairs) {
                if (varPair.to.isChange) {
                    varPair.from.isChange = true;
                    flag = true;
                }
            }
            if (flag) {
                from.isChange = true;
            }
        }

        for (int i = 0; i < wrappers.size(); i++) {
            UnitWrapper uw = wrappers.get(i);
            if (uw.isChange) {
                VarChain varChain = new VarChain();
                // Change stmt as the entry point of the var chain
                //This var is actually inaccurate; it should be fewer.
                List<Var> vars = uw.parsedStmt.getUsedVars();
                varChain.addVars(vars);
                forwardAnalysis(wrappers, uw, i, varChain);
//                backwardAnalysis(wrappers, uw, i,varChain);
                // check
                //Is there a param on the varchain
//                varChain.haveParams(paramVars,node);
//                varChain.haveRetVars(node);

            }
        }
    }

//        List<UnitWrapper> paramUnitWrappers = DataFlowParse.getParamUnit( node);
//        VarChain varChain = new VarChain();
//
//        List<Var>  doChangedVars = new ArrayList<>();
//        for (UnitWrapper paramUnit : paramUnitWrappers) {
//            ParameterStmt pa =  (ParameterStmt) paramUnit.parsedStmt;
//            varChain.addAnchor(pa.leftVar);
//        }
//        DataFlowParse.beginFlow(node,paramUnitWrappers.get(paramUnitWrappers.size()-1) ,paramUnitWrappers.get(paramUnitWrappers.size()-1).successorUnitWrapper, varChain, doChangedVars);
//        List<Var> changedParams = varChain.traceChangedVarBackwardsToFindChangedParams(doChangedVars);
//        //todo set method IO
//        if (node.getChildren() != null && node.getChildren().size() != 0) {
//            return;
//        }

    private void backwardAnalysis(List<UnitWrapper> wrappers, UnitWrapper uw, int index, VarChain varChain) {
        nextUnitWrapper(uw, CallGraphConstant.BACKWARD, varChain, 0);
    }


    private void forwardAnalysis(List<UnitWrapper> wrappers, UnitWrapper uw, int index, VarChain varChain) {
        nextUnitWrapper(uw, CallGraphConstant.FORWARD, varChain, 0);
    }

    private void applyVarLink(UnitWrapper uw, VarChain chain, Var left, List<Var> right, int forwardOrBackward) {
        int linkType = uw.getAssignmentType();
        if (forwardOrBackward == CallGraphConstant.BACKWARD) {
            if (right.size() == 1) {
                chain.applyVarLink(left, right, linkType, forwardOrBackward);
            }
        } else {
            chain.applyVarLink(left, right, linkType, forwardOrBackward);
        }

    }

    private int doSomethingToUw(UnitWrapper uw, VarChain varChain, int isControl, int forwardOrBackward) {
        if (uw.parsedStmt instanceof IfStmt) {
            // If the variables in the if statement overlap with other variables
            if (isControl == CallGraphConstant.IS_CONTROL) {
                return CallGraphConstant.IS_CONTROL;
            }
            IfStmt ifStmt = (IfStmt) uw.parsedStmt;
            if (varChain.hasVar(ifStmt.opt1, forwardOrBackward) || varChain.hasVar(ifStmt.opt2, forwardOrBackward)) {
                return CallGraphConstant.IS_CONTROL;
            }
            return 0;
        }
        if (uw.parsedStmt.isAssignment()) {
            Var left = uw.parsedStmt.getLeftValue();
            List<Var> right = uw.parsedStmt.getRightVars();
            if (forwardOrBackward == CallGraphConstant.FORWARD) {
                if (isControl != CallGraphConstant.IS_CONTROL) {
                    // forward
                    if (varChain.hasVar(right, forwardOrBackward)) {
                        applyVarLink(uw, varChain, left, right, forwardOrBackward);
                    }
                } else {
                    // forward + if affects all variables inside the if block
                    if (varChain.hasVar(right, forwardOrBackward)) {
                        applyVarLink(uw, varChain, left, right, forwardOrBackward);

                    } else {
                        varChain.addAnchor(left);
                    }
                }
            } else if (forwardOrBackward == CallGraphConstant.BACKWARD) {
                if (varChain.hasVar(left, forwardOrBackward)) {
                    applyVarLink(uw, varChain, left, right, forwardOrBackward);
                }
            }

            return 0;
        }
        if (uw.parsedStmt instanceof ReturnStmt) {
            ReturnStmt returnStmt = (ReturnStmt) uw.parsedStmt;
            if (!returnStmt.isVoid) {
                varChain.addRetVar(returnStmt.var);
            }
        }

        return 0;

    }

    private void nextUnitWrapper(UnitWrapper uw, int forwardOrBackward, VarChain varChain, int isControl) {
        int res = doSomethingToUw(uw, varChain, isControl, forwardOrBackward);
        uw.isVisited = true;
        List<UnitWrapper> next = null;
        if (forwardOrBackward == CallGraphConstant.BACKWARD) {
            next = uw.predecessorUnitWrapper;
        } else if (forwardOrBackward == CallGraphConstant.FORWARD) {
            next = uw.successorUnitWrapper;
        }
        for (UnitWrapper u : next) {
            if (u.isVisited) {
                continue;
            }
            nextUnitWrapper(u, forwardOrBackward, varChain, res);
        }
    }

}