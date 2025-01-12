package com.magneto.staticanalysis.callgraph.analysis.dataflow;


import lombok.extern.slf4j.Slf4j;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.*;

import java.util.List;

@Slf4j
public class AbstractStmtFactory {
    public static AbstractStmt createSwitchStmt(JLookupSwitchStmt jLookupSwitchStmt) {
        Value v = jLookupSwitchStmt.getKey();
        return new TableSwitchStmt(v);
    }

    public static AbstractStmt createSwitchStmt(JTableSwitchStmt jTableSwitchStmt) {
        Value v = jTableSwitchStmt.getKey();
        return new TableSwitchStmt(v);
    }

    public static AbstractStmt createIdentityStmt(Value v) {
        ParameterStmt parameterStmt = new ParameterStmt(v);
        return parameterStmt;
    }

    public static AssignmentInvokeStmt createAssignmentInvokeStmt(Value left, Value right) {
        if (right instanceof JNewExpr) {
            JNewExpr jNewExpr = (JNewExpr) right;
            Type type = jNewExpr.getType();
            SootClass sootClass = jNewExpr.getBaseType().getSootClass();
            return new AssignmentInvokeStmt(left, right, null, sootClass);
        } else if (right instanceof JVirtualInvokeExpr) {
            JVirtualInvokeExpr jVirtualInvokeExpr = (JVirtualInvokeExpr) right;
            Value base = jVirtualInvokeExpr.getBase();
            SootMethod sootMethod = jVirtualInvokeExpr.getMethod();
            List<Value> args = jVirtualInvokeExpr.getArgs();
            return new AssignmentInvokeStmt(left, base, args, sootMethod);
        } else if (right instanceof JInterfaceInvokeExpr) {
            JInterfaceInvokeExpr interfaceInvokeExpr = (JInterfaceInvokeExpr) right;
            Value base = interfaceInvokeExpr.getBase();
            try {
                SootMethod sootMethod = interfaceInvokeExpr.getMethod();
                List<Value> args = interfaceInvokeExpr.getArgs();
                return new AssignmentInvokeStmt(left, base, args, sootMethod);
            } catch (Exception e) {
                log.warn("MethodRetrieveWrong: ", e);
            }
        } else if (right instanceof JStaticInvokeExpr) {
            JStaticInvokeExpr jStaticInvokeExpr = (JStaticInvokeExpr) right;
            Type type = jStaticInvokeExpr.getType();
            SootMethod sootMethod = jStaticInvokeExpr.getMethod();
            List<Value> args = jStaticInvokeExpr.getArgs();
            return new AssignmentInvokeStmt(left, type, args, sootMethod);
        } else if (right instanceof JSpecialInvokeExpr) {
            JSpecialInvokeExpr jSpecialInvokeExpr = (JSpecialInvokeExpr) right;
            Type type = jSpecialInvokeExpr.getType();
            SootMethod sootMethod = jSpecialInvokeExpr.getMethod();
            List<Value> args = jSpecialInvokeExpr.getArgs();
            return new AssignmentInvokeStmt(left, type, args, sootMethod);
        }
//        log.warn("new case");
        return null;
    }

    public static AssignmentStmt createAssignmentStmt(Value left, Value right) {
        List<Value> newList = ExprUtil.exprToValueList(right);
        AssignmentStmt assignmentStmt = new AssignmentStmt(left, newList);
        assignmentStmt.assignmentOpt = right.getClass();
        return assignmentStmt;
    }

    public static InvokeStmt createInvokeStmt(InvokeExpr ie) {
        if (ie instanceof JSpecialInvokeExpr) {
            JSpecialInvokeExpr jsie = (JSpecialInvokeExpr) ie;
            Value base = jsie.getBase();
            List<Value> args = jsie.getArgs();
            SootMethod sootMethod = jsie.getMethod();
            return new InvokeStmt(base, args, sootMethod);
        } else if (ie instanceof JInterfaceInvokeExpr) {
            JInterfaceInvokeExpr jiie = (JInterfaceInvokeExpr) ie;
            try {
                SootMethod sootMethod = jiie.getMethod();
                Value base = jiie.getBase();
                List<Value> args = jiie.getArgs();
                return new InvokeStmt(base, args, sootMethod);
            } catch (Exception e) {
                log.error("MethodRetrieveWrong: ", e);
            }
        } else if (ie instanceof JStaticInvokeExpr) {
            JStaticInvokeExpr jst = (JStaticInvokeExpr) ie;
            List<Value> args = jst.getArgs();
            SootMethod sootMethod = jst.getMethod();

            return new InvokeStmt(null, args, sootMethod);
        } else if (ie instanceof JVirtualInvokeExpr) {
            JVirtualInvokeExpr jvie = (JVirtualInvokeExpr) ie;
            Value base = jvie.getBase();
            List<Value> values = jvie.getArgs();
            SootMethod sootMethod = jvie.getMethod();
            return new InvokeStmt(base, values, sootMethod);
        }
//        log.warn("new case");
        return null;

    }

    public static IfStmt createIfStmt(Value v) {
        return new IfStmt(v);
    }

    public static ReturnStmt createReturnStmt(Value v) {
        return new ReturnStmt(v);
    }

    public static GotoStmt createGotoStmt(int id) {
        return new GotoStmt(id);
    }

    public static ThrowStmt createThrowStmt(Value v) {
        return new ThrowStmt(v);
    }
}
