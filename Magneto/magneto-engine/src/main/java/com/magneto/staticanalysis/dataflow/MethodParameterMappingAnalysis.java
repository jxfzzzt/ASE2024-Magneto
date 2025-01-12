package com.magneto.staticanalysis.dataflow;

import com.magneto.staticanalysis.MethodCall;
import com.magneto.staticanalysis.taint.*;
import com.magneto.util.ReflectionUtil;
import lombok.NonNull;
import soot.*;
import soot.jimple.BinopExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

import java.util.*;

public class MethodParameterMappingAnalysis extends TaintFlowAnalysis {

    private final MethodCall sourceMethod;

    private final MethodCall triggeredMethod;

    private final Body body;

    private final Map<Integer, Set<TaintObject>> taintObjectMap;

    private final Set<TaintObject> taintObjectSet;

    public MethodParameterMappingAnalysis(@NonNull MethodCall sourceMethod, @NonNull MethodCall triggeredMethod) {
        super();
        this.sourceMethod = sourceMethod;
        this.taintObjectMap = new HashMap<>();
        this.triggeredMethod = triggeredMethod;
        this.taintObjectSet = new HashSet<>();
        this.body = sourceMethod.getSootMethod().retrieveActiveBody();
    }

    @Override
    public Set<TaintObject> doAnalysis() {
        UnitGraph cfg = new ExceptionalUnitGraph(this.body);
        List<Unit> targetUnitList = findTriggeredMethodUnit(cfg, triggeredMethod.getSootMethod());
        Integer argNum = triggeredMethod.getArgNum();


        for (Unit unit : targetUnitList) {
            for (int i = 0; i < argNum; i++) {
                taintObjectSet.clear();
                Set<Unit> visitedUnitSet = new HashSet<>();
                super.clearFlowAbstractions();

                intraTaintAnalysis(null, sourceMethod.getSootMethod(), unit, i, 1, visitedUnitSet);

                Set<TaintObject> taintObjects = taintObjectMap.getOrDefault(i, new HashSet<>());
                taintObjects.addAll(taintObjectSet);
                taintObjectMap.put(i, taintObjects);
            }
        }
        return taintObjectSet;
    }


    private void intraTaintAnalysis(List<Value> sourceInputParams, SootMethod sootMethod, Unit currentUnit, int argIndex, int depth, Set<Unit> visitedUnitSet) {
        if (depth > MAX_ANALYSIS_DEPTH) return;
        Queue<Unit> queue = new LinkedList<>();
        queue.add(currentUnit);
        UnitGraph graph = new ExceptionalUnitGraph(sootMethod.retrieveActiveBody());

        while (!queue.isEmpty()) {
            Unit node = queue.poll();
            visitedUnitSet.add(node);
            analysisUnit(sourceInputParams, sootMethod, node, argIndex, depth);

            List<Unit> predUnits = graph.getPredsOf(node);
            for (Unit unit : predUnits) {
                if (!visitedUnitSet.contains(unit)) {
                    queue.add(unit);
                }
            }
        }
    }


    protected void analysisUnit(List<Value> sourceInputParams, SootMethod sootMethod, Unit unit, int argIndex, int depth) {
        if (depth > MAX_ANALYSIS_DEPTH) return;

        if (unit instanceof JInvokeStmt) {
            JInvokeStmt invokeStmt = (JInvokeStmt) unit;
            InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();
            SootMethod invokeMethod = invokeExpr.getMethod();

            // check whether is triggered method
            if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                List<Value> args = invokeExpr.getArgs();
                for (int i = 0; i < args.size(); i++) {
                    Value arg = args.get(i);
                    if (i == argIndex && arg instanceof Local) { // need to check arg index is same
                        Local local = (Local) arg;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                }
            } else if (invokeExpr instanceof JSpecialInvokeExpr) {
                // resolve the constructor method
                JSpecialInvokeExpr specialInvokeExpr = (JSpecialInvokeExpr) invokeExpr;
                Value base = specialInvokeExpr.getBase();
                if (checkIsTaint(base)) {
                    List<Value> args = specialInvokeExpr.getArgs();
                    for (Value arg : args) {
                        if (arg instanceof Local) {
                            Local local = (Local) arg;
                            FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                            flowAbstractionSet.add(flowAbstraction);
                        }
                    }
                }
            }
        } else if (unit instanceof JAssignStmt) {
            JAssignStmt assignStmt = (JAssignStmt) unit;
            Value leftOp = assignStmt.getLeftOp();
            Value rightOp = assignStmt.getRightOp();

            // First, determine if it is a triggered method
            if (rightOp instanceof JVirtualInvokeExpr
                    || rightOp instanceof JSpecialInvokeExpr
                    || rightOp instanceof JStaticInvokeExpr
                    || rightOp instanceof JInterfaceInvokeExpr) {
                InvokeExpr invokeExpr = (InvokeExpr) rightOp;
                SootMethod invokeMethod = invokeExpr.getMethod();
                if (checkIsTriggeredMethod(triggeredMethod.getSootMethod(), invokeMethod)) {
                    List<Value> args = invokeExpr.getArgs();
                    for (int i = 0; i < args.size(); i++) {
                        Value arg = args.get(i);
                        if (i == argIndex && arg instanceof Local) { // need to check arg index is same
                            Local local = (Local) arg;
                            FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                            flowAbstractionSet.add(flowAbstraction);
                        }
                    }
                    return;
                }
            }
            if (checkIsTaint(leftOp)) { // taint flow analysis part
                FlowAbstraction taintFlowAbstraction = super.getTaintFlowAbstraction(leftOp);

                if (rightOp instanceof JimpleLocal) {
                    JimpleLocal local = (JimpleLocal) rightOp;
                    FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                    flowAbstractionSet.add(flowAbstraction);
                } else if (rightOp instanceof JArrayRef) {
                    JArrayRef arrayRef = (JArrayRef) rightOp;
                    Value base = arrayRef.getBase();
                    Value index = arrayRef.getIndex();
                    if (base instanceof Local) {
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, (Local) base);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                    if (index instanceof Local) {
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, (Local) index);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                } else if (rightOp instanceof JInstanceFieldRef) {
                    JInstanceFieldRef instanceFieldRef = (JInstanceFieldRef) rightOp;
                    Local local = (Local) instanceFieldRef.getBase();
                    SootField field = instanceFieldRef.getField();
                    if (field.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                        // add taint field
                        TaintField taintField = new TaintField(field, sourceMethod.getClazz(), taintFlowAbstraction.getAttributes());
                        taintObjectSet.add(taintField);
                        // add to flowAbstractionSet
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local, field, taintFlowAbstraction.getAttributes());
                        flowAbstractionSet.add(flowAbstraction);
                    } else {
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local, field, taintFlowAbstraction.getAttributes());
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
                    SootMethod invokeMethod = virtualInvokeExpr.getMethod();

                    if (invokeMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                        // If it is a method call from a source class
                        List<Value> args = virtualInvokeExpr.getArgs();
                        depthFirstTaintAnalysis(args, invokeMethod, argIndex, depth + 1);
                    } else {
                        List<Value> args = virtualInvokeExpr.getArgs();
                        for (Value arg : args) {
                            if (arg instanceof Local) {
                                Local local = (Local) arg;
                                FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                                flowAbstractionSet.add(flowAbstraction);
                            }
                        }

                        // resolve .toString(), .toJSONString(), .getString("xxx")
                        Value base = virtualInvokeExpr.getBase();
                        if (base instanceof Local) {
                            Local local = (Local) base;
                            FlowAbstraction flowAbstraction;

                            // if is getXXX method, need to record the corresponding attribute
                            if (args.isEmpty() && ReflectionUtil.isGetMethod(invokeMethod.getName())) {
                                flowAbstraction = new FlowAbstraction(unit, local,
                                        null, new HashSet<>(Collections.singleton(ReflectionUtil.attributeFromGetMethod(invokeMethod.getName()))));
                            } else {
                                flowAbstraction = new FlowAbstraction(unit, local);
                            }
                            flowAbstractionSet.add(flowAbstraction);
                        }
                    }
                } else if (rightOp instanceof JSpecialInvokeExpr) {
                    JSpecialInvokeExpr specialInvokeExpr = (JSpecialInvokeExpr) rightOp;
                    SootMethod invokeMethod = specialInvokeExpr.getMethod();

                    if (invokeMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                        List<Value> args = specialInvokeExpr.getArgs();
                        depthFirstTaintAnalysis(args, invokeMethod, argIndex, depth + 1);
                    } else {
                        List<Value> args = specialInvokeExpr.getArgs();
                        for (Value arg : args) {
                            if (arg instanceof Local) {
                                Local local = (Local) arg;
                                FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                                flowAbstractionSet.add(flowAbstraction);
                            }
                        }
                    }

                } else if (rightOp instanceof JStaticInvokeExpr) {
                    JStaticInvokeExpr staticInvokeExpr = (JStaticInvokeExpr) rightOp;
                    SootMethod invokeMethod = staticInvokeExpr.getMethod();

                    if (invokeMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                        List<Value> args = staticInvokeExpr.getArgs();
                        depthFirstTaintAnalysis(args, invokeMethod, argIndex, depth + 1);
                    } else {
                        List<Value> args = staticInvokeExpr.getArgs();
                        for (Value arg : args) {
                            if (arg instanceof Local) {
                                Local local = (Local) arg;
                                FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                                flowAbstractionSet.add(flowAbstraction);
                            }
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
                } else if (rightOp instanceof BinopExpr) {
                    BinopExpr binopExpr = (BinopExpr) rightOp;
                    Value op1 = binopExpr.getOp1();
                    Value op2 = binopExpr.getOp2();
                    if (op1 instanceof Local) {
                        Local local = (Local) op1;
                        FlowAbstraction flowAbstraction = new FlowAbstraction(unit, local);
                        flowAbstractionSet.add(flowAbstraction);
                    }
                    if (op2 instanceof Local) {
                        Local local = (Local) op2;
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
        } else if (unit instanceof JIdentityStmt) {
            JIdentityStmt identityStmt = (JIdentityStmt) unit;
            Value leftValue = identityStmt.getLeftOp();
            if (checkIsTaint(leftValue)) {
                FlowAbstraction taintFlowAbstraction = super.getTaintFlowAbstraction(leftValue);
                Local leftLocal = (Local) leftValue;
                Body body = sootMethod.retrieveActiveBody();
                List<Local> parameterLocals = body.getParameterLocals();
                int i = parameterLocals.indexOf(leftLocal);
                if (i != -1
                        && sootMethod.getDeclaringClass().getName().equals(sourceMethod.getClazz().getName())) {
                    if (sootMethod.getSignature().equals(sourceMethod.getMethodSignature())) {
                        // add a new tainted parameter
                        TaintParam taintParam = new TaintParam(sootMethod, i, sourceMethod.getClazz(), taintFlowAbstraction.getAttributes());
                        taintObjectSet.add(taintParam);
                    } else {
                        assert i < sourceInputParams.size();
                        // add a flow abstract
                        Value value = sourceInputParams.get(i);
                        if (value instanceof Local) {
                            FlowAbstraction flowAbstraction = new FlowAbstraction(unit, (Local) value, null, taintFlowAbstraction.getAttributes());
                            flowAbstractionSet.add(flowAbstraction);
                        }
                    }
                }
            }
        }
    }

    private void depthFirstTaintAnalysis(List<Value> sourceInputParams, SootMethod sootMethod, int argIndex, int depth) {
        if (depth > MAX_ANALYSIS_DEPTH) return;
        if (sootMethod.getSignature().equals(triggeredMethod.getMethodSignature())) return;

        UnitGraph cfg = new ExceptionalUnitGraph(sootMethod.retrieveActiveBody());
        List<Unit> tails = cfg.getTails();
        Set<Unit> visitedUnitSet = new HashSet<>();
        for (Unit unit : tails) {
            intraTaintAnalysis(sourceInputParams, sootMethod, unit, argIndex, depth, visitedUnitSet);
        }
    }

    public Map<Integer, Set<TaintObject>> getTaintObjectMap() {
        return taintObjectMap;
    }

    public MethodCall getSourceMethod() {
        return sourceMethod;
    }

    public MethodCall getTriggeredMethod() {
        return triggeredMethod;
    }

    public Body getBody() {
        return body;
    }

    public Set<TaintObject> getTaintObjectSet() {
        return taintObjectSet;
    }

}
