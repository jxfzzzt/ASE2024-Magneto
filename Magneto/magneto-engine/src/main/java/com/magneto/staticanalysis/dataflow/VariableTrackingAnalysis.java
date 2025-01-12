package com.magneto.staticanalysis.dataflow;

import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.taint.*;
import lombok.NonNull;
import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.*;

public class VariableTrackingAnalysis extends TaintFlowAnalysis {

    private final MethodCall sourceMethod;

    private final MethodCall triggeredMethod;

    private final Body body;

    private final Set<TaintObject> taintObjectSet;

    public VariableTrackingAnalysis(@NonNull MethodCall sourceMethod, @NonNull MethodCall triggeredMethod) {
        super();
        this.sourceMethod = sourceMethod;
        this.triggeredMethod = triggeredMethod;
        this.taintObjectSet = new HashSet<>();
        this.body = sourceMethod.getSootMethod().retrieveActiveBody();
    }

    // pure intra analysis
    @Override
    public Set<TaintObject> doAnalysis() {
        UnitGraph cfg = new ExceptionalUnitGraph(this.body);
        List<Unit> targetUnitList = findTriggeredMethodUnit(cfg, triggeredMethod.getSootMethod());
        Set<Unit> visitedUnitSet = new HashSet<>();
        for (Unit unit : targetUnitList) {
            intraTaintAnalysis(null, sourceMethod.getSootMethod(), unit, 1, visitedUnitSet);
        }
        return taintObjectSet;
    }

    private void intraTaintAnalysis(List<Value> sourceInputParams, SootMethod sootMethod, Unit currentUnit, int depth, Set<Unit> visitedUnitSet) {
        if (depth > MAX_ANALYSIS_DEPTH) return;
        Queue<Unit> queue = new LinkedList<>();
        queue.add(currentUnit);
        UnitGraph graph = new ExceptionalUnitGraph(sootMethod.retrieveActiveBody());

        while (!queue.isEmpty()) {
            Unit node = queue.poll();
            visitedUnitSet.add(node);
            analysisUnit(sourceInputParams, sootMethod, node, depth);

            List<Unit> predUnits = graph.getPredsOf(node);
            for (Unit unit : predUnits) {
                if (!visitedUnitSet.contains(unit)) {
                    queue.add(unit);
                }
            }
        }
    }

    protected void analysisUnit(List<Value> sourceInputParams, SootMethod sootMethod, Unit unit, int depth) {
        if (depth > MAX_ANALYSIS_DEPTH) return;
        if (unit instanceof JIdentityStmt) {
            JIdentityStmt identityStmt = (JIdentityStmt) unit;
            Value leftValue = identityStmt.getLeftOp();
            if (checkIsTaint(leftValue)) {
                Local leftLocal = (Local) leftValue;
                Body body = sootMethod.retrieveActiveBody();
                List<Local> parameterLocals = body.getParameterLocals();
                int i = parameterLocals.indexOf(leftLocal);
                if (i != -1
                        && sootMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                    if (sootMethod.getSignature().equals(sourceMethod.getMethodSignature())) {
                        // add a new tainted parameter
                        TaintParam taintParam = new TaintParam(sootMethod, i, sourceMethod.getClazz(), null);
                        taintObjectSet.add(taintParam);
                    }
                }
            }
        } else if (unit instanceof JAssignStmt) {
            JAssignStmt assignStmt = (JAssignStmt) unit;
            Value leftOp = assignStmt.getLeftOp();
            Value rightOp = assignStmt.getRightOp();

            // add taint source
            if (rightOp instanceof JVirtualInvokeExpr) {
                JVirtualInvokeExpr virtualInvokeExpr = (JVirtualInvokeExpr) rightOp;
                SootMethod invokeMethod = virtualInvokeExpr.getMethod();
                if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                    Value base = virtualInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                        return;
                    }
                }
            } else if (rightOp instanceof JSpecialInvokeExpr) {
                JSpecialInvokeExpr specialInvokeExpr = (JSpecialInvokeExpr) rightOp;
                SootMethod invokeMethod = specialInvokeExpr.getMethod();
                if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                    Value base = specialInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                        return;
                    }
                }
            } else if (rightOp instanceof JInterfaceInvokeExpr) {
                JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) rightOp;
                SootMethod invokeMethod = interfaceInvokeExpr.getMethod();
                if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                    Value base = interfaceInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        if (!"this".equals(local.getName())) {
                            FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                            flowAbstractionSet.add(flowAbstraction);
                            return;
                        }
                    }
                }
            }

            // taint flow through
            if (checkIsTaint(leftOp)) {
                if (rightOp instanceof JimpleLocal) {
                    JimpleLocal local = (JimpleLocal) rightOp;
                    FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                    flowAbstractionSet.add(flowAbstraction);
                } else if (rightOp instanceof JInstanceFieldRef) {
                    JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef) rightOp;
                    SootField field = instanceFieldRef.getField();
                    if (field.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                        TaintField taintField = new TaintField(field, sourceMethod.getClazz(), null);
                        taintObjectSet.add(taintField);
                    }
                } else if (rightOp instanceof JArrayRef) {
                    JArrayRef arrayRef = (JArrayRef) rightOp;
                    Value base = arrayRef.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                } else if (rightOp instanceof JCastExpr) {
                    JCastExpr castExpr = (JCastExpr) rightOp;
                    Value op = castExpr.getOp();
                    if (op instanceof Local) {
                        Local local = (Local) op;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                } else if (rightOp instanceof JVirtualInvokeExpr) {
                    JVirtualInvokeExpr virtualInvokeExpr = (JVirtualInvokeExpr) rightOp;
                    Value base = virtualInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        SootMethod invokeMethod = virtualInvokeExpr.getMethod();
                        if (invokeMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())
                                && "this".equals(local.getName())) {
                            List<Value> args = virtualInvokeExpr.getArgs();
                            depthFirstTaintAnalysis(args, invokeMethod, depth + 1);
                        }
                    }
                } else if (rightOp instanceof JSpecialInvokeExpr) {
                    JSpecialInvokeExpr specialInvokeExpr = (JSpecialInvokeExpr) rightOp;
                    Value base = specialInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        SootMethod invokeMethod = specialInvokeExpr.getMethod();
                        if (invokeMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName()) &&
                                "this".equals(local.getName())) {
                            List<Value> args = specialInvokeExpr.getArgs();
                            depthFirstTaintAnalysis(args, invokeMethod, depth + 1);
                        }
                    }
                } else if (rightOp instanceof JInterfaceInvokeExpr) {
                    JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) rightOp;
                    Value base = interfaceInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        if (!"this".equals(local.getName())) {
                            FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                            flowAbstractionSet.add(flowAbstraction);
                        }
                    }
                }
            }
        } else if (unit instanceof JInvokeStmt) {
            JInvokeStmt invokeStmt = (JInvokeStmt) unit;
            InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();
            SootMethod invokeMethod = invokeExpr.getMethod(); // triggered method
            if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                if (invokeExpr instanceof JVirtualInvokeExpr) {
                    JVirtualInvokeExpr virtualInvokeExpr = (JVirtualInvokeExpr) invokeExpr;
                    Value base = virtualInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                } else if (invokeExpr instanceof JSpecialInvokeExpr) {
                    JSpecialInvokeExpr specialInvokeExpr = (JSpecialInvokeExpr) invokeExpr;
                    Value base = specialInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                } else if (invokeExpr instanceof JInterfaceInvokeExpr) {
                    JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) invokeExpr;
                    Value base = interfaceInvokeExpr.getBase();
                    if (base instanceof Local) {
                        Local local = (Local) base;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                }
            }
        } else if (unit instanceof JReturnStmt) {
            if (depth == 1) return;
            JReturnStmt returnStmt = (JReturnStmt) unit;
            Value op = returnStmt.getOp();
            if (op instanceof Local) {
                Local local = (Local) op;
                // default taint
                FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                flowAbstractionSet.add(flowAbstraction);
            }
        }
    }

    private void depthFirstTaintAnalysis(List<Value> sourceInputParams, SootMethod sootMethod, int depth) {
        if (depth > MAX_ANALYSIS_DEPTH) return;
        if (sootMethod.getSignature().equals(triggeredMethod.getMethodSignature())) return;

        UnitGraph cfg = new ExceptionalUnitGraph(sootMethod.retrieveActiveBody());
        List<Unit> tails = cfg.getTails();
        Set<Unit> visitedUnitSet = new HashSet<>();
        for (Unit unit : tails) {
            intraTaintAnalysis(sourceInputParams, sootMethod, unit, depth, visitedUnitSet);
        }
    }

    public Set<TaintObject> getTaintObjectSet() {
        return taintObjectSet;
    }

    public Body getBody() {
        return body;
    }

    public MethodCall getTriggeredMethod() {
        return triggeredMethod;
    }

    public MethodCall getSourceMethod() {
        return sourceMethod;
    }
}
