package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.*;
import com.magneto.staticanalysis.callgraph.global.CallGraphConstant;
import lombok.extern.slf4j.Slf4j;
import soot.jimple.Constant;
import soot.jimple.internal.JIdentityStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * core
 */
@Slf4j
public class DataFlowParse {

    /**
     * The input is a param list; the output checks if the param list will change after passing through all successor nodes
     *
     * @param node
     * @param successorUnits
     * @return
     */
    public static boolean beginFlow(Node node, UnitWrapper prevUnit, List<UnitWrapper> successorUnits, VarChain varChain, List<Var> changedUnitId) {
        if (successorUnits.isEmpty()) return false;
        for (UnitWrapper successor : successorUnits) {
            if (successor.parsedStmt instanceof ParameterStmt) {
                beginFlow(node, prevUnit, successor.successorUnitWrapper, varChain, changedUnitId);
            } else if (successor.parsedStmt instanceof AssignmentInvokeStmt) {
                isSuccessorInvokeStmt(prevUnit, successor, node, varChain, changedUnitId, true);
            } else if (successor.parsedStmt instanceof AssignmentStmt) {
                isSuccessorAssigment(prevUnit, successor, node, varChain, changedUnitId);
            } else if (successor.parsedStmt instanceof InvokeStmt) {
                isSuccessorInvokeStmt(prevUnit, successor, node, varChain, changedUnitId, false);
            } else if (successor.parsedStmt instanceof ReturnStmt) {
                isSetReturnVarChange(prevUnit, successor, node, varChain, changedUnitId);
            } else if (successor.parsedStmt instanceof IfStmt) {
                isIfStmt(prevUnit, successor, node, varChain, changedUnitId);
            } else {
//                log.warn("new case");
            }
        }
        return true;
    }

    private static void isIfStmt(UnitWrapper prevUnit, UnitWrapper successor, Node node, VarChain varChain, List<Var> changedUnitId) {
        IfStmt ifStmt = (IfStmt) successor.parsedStmt;
        Var opt1 = ifStmt.opt1;
        Var opt2 = ifStmt.opt2;
        if (opt1.isConstant && !opt2.isConstant) {
            if (varChain.hasVar(opt2, CallGraphConstant.FORWARD)) {
                Var prev = varChain.getSameVarName(opt2, CallGraphConstant.FORWARD);
                varChain.addVarLink(prev, opt2, CallGraphConstant.VarLinkType.LINK0);
            }
        }


        if (!opt1.isConstant && opt2.isConstant) {
            if (varChain.hasVar(opt1, CallGraphConstant.FORWARD)) {
                Var prev = varChain.getSameVarName(opt1, CallGraphConstant.FORWARD);
                varChain.addVarLink(prev, opt1, CallGraphConstant.VarLinkType.LINK0);
            }
        }
        beginFlow(node, successor, successor.successorUnitWrapper, varChain, changedUnitId);

    }

    private static void isSetReturnVarChange(UnitWrapper prevUnit, UnitWrapper successor, Node node, VarChain varChain, List<Var> changedUnitId) {
        ReturnStmt returnStmt = (ReturnStmt) successor.parsedStmt;
        if (returnStmt.isVoid) {
            return;
        }
        if (!returnStmt.var.isConstant) {
            if (varChain.hasVar(returnStmt.var, CallGraphConstant.FORWARD)) {
                Var prev = varChain.getSameVarName(returnStmt.var, CallGraphConstant.FORWARD);
                varChain.addVarLink(prev, returnStmt.var, CallGraphConstant.VarLinkType.LINK0);
//            returnStmt.var.isChange = true;
            }
        } else {
            varChain.addVarLink(varChain.newAddedVar, returnStmt.var, CallGraphConstant.VarLinkType.LINK0);
        }
    }

    /**
     * Whether to include the left-hand side of the Invoke depends on whether this statement changes
     *
     * @param successor
     * @param node
     */
    private static void isSuccessorInvokeStmt(UnitWrapper prevUnit, UnitWrapper successor, Node node, VarChain varChain, List<Var> changedUnitId, boolean isAssignmentInvoke) {
        UnitWrapperToNodePair child = node.getMappedNodeOfUnitWrapper(successor);
        List<VarPair> varPair = child.getBaseAndArgsPair();
        boolean haveIsChange = false;
        List<VarPair> isChangeVp = new ArrayList<>();
        for (VarPair vp : varPair) {
            if (vp.to.isChange) {
                vp.from.isChange = true;
                haveIsChange = true;
                isChangeVp.add(vp);
            }
        }
        if (haveIsChange) {
            if (isAssignmentInvoke) {
                List<Var> retVar = child.getToInvokedMethod().getNodeIOVars().getChangedReturnVar();
                if (retVar.size() != 0) {
                    AssignmentInvokeStmt assignmentInvokeStmt = (AssignmentInvokeStmt) successor.parsedStmt;
                    assignmentInvokeStmt.leftVar.isChange = true;
                    varChain.addVarLink(null, assignmentInvokeStmt.leftVar, CallGraphConstant.VarLinkType.LINK0);
                }
            }
        }
        beginFlow(node, successor, successor.successorUnitWrapper, varChain, changedUnitId);

    }

    /**
     * In an Assignment statement, the left-hand side value should be added to the Value list
     * a = param
     * a.b = param
     * a = param.b
     * a.b = param.b
     * Currently, treat the above four cases as the same; differentiate them later
     * @param successor
     * @param node
     */
    private static void isSuccessorAssigment(UnitWrapper prevUnit, UnitWrapper successor, Node node, VarChain varChain, List<Var> changedUnitId) {
        AssignmentStmt assignStmt = (AssignmentStmt) successor.parsedStmt;
        Var left = assignStmt.leftVar;
        List<Var> rights = assignStmt.rightVars;
        for (Var right : rights) {
            if (isValuePrimitive(right)) {
                // If it is a primitive type, it does not need to be considered
                continue;
            }
            if (varChain.hasVar(right, CallGraphConstant.FORWARD)) {
                // r0 = r1 + r2; r1 is in the chain
                List<Var> relatedVar = varChain.getRelatedVar(right);
                for (Var relates : relatedVar) {
                    varChain.addVarLink(relates, left, CallGraphConstant.VarLinkType.LINK0);
                }
            }
        }

        beginFlow(node, successor, successor.successorUnitWrapper, varChain, changedUnitId);

    }

    private static boolean isValuePrimitive(Var v) {
        return v.value instanceof Constant;
    }

    public static List<UnitWrapper> getParamUnit(Node cianode) {
        List<UnitWrapper> inputParamUnits = new ArrayList<>();
        for (UnitWrapper unitWrapper : cianode.getUnitWrapperContainer().getUnitWrappers()) {
            if (unitWrapper.unit instanceof JIdentityStmt) {
                inputParamUnits.add(unitWrapper);
            }
        }
        return inputParamUnits;
    }

}
