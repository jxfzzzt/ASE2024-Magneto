package com.magneto.staticanalysis.taint;

import cn.hutool.extra.spring.SpringUtil;
import com.magneto.config.ConfigProperty;
import lombok.NonNull;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.UnitGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TaintFlowAnalysis {

    protected final static Integer MAX_ANALYSIS_DEPTH = SpringUtil.getBean(ConfigProperty.class).getMaxInterAnalysisScope();

    protected final Set<FlowAbstraction> flowAbstractionSet;

    protected TaintFlowAnalysis() {
        this.flowAbstractionSet = new HashSet<>();
    }

    public abstract Set<TaintObject> doAnalysis();

    private boolean checkIsAssignable(@NonNull SootClass targetClass, @NonNull SootClass triggeredClass) {
        if (targetClass.getName().equals(triggeredClass.getName()) || targetClass.implementsInterface(triggeredClass.getName())) {
            return true;
        }

        SootClass currentClass = targetClass;
        while (currentClass.hasSuperclass()) {
            if (currentClass.getSuperclass().getName().equals(triggeredClass.getName())) {
                return true;
            }

            if (targetClass.getSuperclass().implementsInterface(triggeredClass.getName())) {
                return true;
            }
            currentClass = currentClass.getSuperclass();
        }

        return false;
    }

    protected void clearFlowAbstractions() {
        flowAbstractionSet.clear();
    }

    protected boolean checkIsTriggeredMethod(SootMethod triggeredMethod, SootMethod invokeMethod) {
        if (triggeredMethod.getSignature().equals(invokeMethod.getSignature())) {
            return true;
        } else if (triggeredMethod.getSubSignature().equals(invokeMethod.getSubSignature())
                && checkIsAssignable(triggeredMethod.getDeclaringClass(), invokeMethod.getDeclaringClass())) {
            return true;
        } else {
            return false;
        }
    }

    // find triggered method unit
    protected List<Unit> findTriggeredMethodUnit(@NonNull UnitGraph graph, @NonNull SootMethod triggeredMethod) {
        List<Unit> targetUnitList = new ArrayList<>();
        for (Unit unit : graph) {
            Stmt s = (Stmt) unit;
            if (s.containsInvokeExpr()) {
                if (s instanceof JAssignStmt) {
                    JAssignStmt assignStmt = (JAssignStmt) s;
                    Value rightOp = assignStmt.getRightOp();
                    if (rightOp instanceof InvokeExpr) {
                        InvokeExpr invokeExpr = (InvokeExpr) rightOp;
                        SootMethod invokeMethod = invokeExpr.getMethod();
                        if (checkIsTriggeredMethod(triggeredMethod, invokeMethod)) {
                            targetUnitList.add(unit);
                        }
                    }
                } else if (s instanceof JInvokeStmt) {
                    JInvokeStmt invokeStmt = (JInvokeStmt) s;
                    InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();
                    SootMethod invokeMethod = invokeExpr.getMethod();
                    if (checkIsTriggeredMethod(triggeredMethod, invokeMethod)) {
                        targetUnitList.add(unit);
                    }
                }
            }
        }
        return targetUnitList;
    }

    // check whether value is tainted
    protected boolean checkIsTaint(Value value) {
        if (value instanceof Local) {
            Local local = (Local) value;
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())) {
                    return true;
                }
            }
        } else if (value instanceof JInstanceFieldRef) {
            JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef) value;
            SootField field = instanceFieldRef.getField();
            assert field != null;
            Local local = (Local) instanceFieldRef.getBase();
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())
                        && flowAbstraction.getField() != null
                        && field.getSignature().equals(flowAbstraction.getField().getSignature())) {
                    return true;
                }
            }
        } else if (value instanceof JArrayRef) {
            JArrayRef arrayRef = (JArrayRef) value;
            Local local = (Local) arrayRef.getBase();
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())) {
                    return true;
                }
            }
        }
        return false;
    }

    // get FlowAbstraction from set
    protected FlowAbstraction getTaintFlowAbstraction(Value value) {
        if (value instanceof Local) {
            Local local = (Local) value;
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())) {
                    return flowAbstraction;
                }
            }
        } else if (value instanceof JInstanceFieldRef) {
            JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef) value;
            SootField field = instanceFieldRef.getField();
            assert field != null;
            Local local = (Local) instanceFieldRef.getBase();
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())
                        && flowAbstraction.getField() != null
                        && field.getSignature().equals(flowAbstraction.getField().getSignature())) {
                    return flowAbstraction;
                }
            }
        } else if (value instanceof JArrayRef) {
            JArrayRef arrayRef = (JArrayRef) value;
            Local local = (Local) arrayRef.getBase();
            for (FlowAbstraction flowAbstraction : flowAbstractionSet) {
                if (local.equals(flowAbstraction.getLocal())) {
                    return flowAbstraction;
                }
            }
        }
        return null;
    }
}
