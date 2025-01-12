package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import soot.Value;
import soot.jimple.Constant;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.*;

import java.util.ArrayList;
import java.util.List;

public class ExprUtil {

    public static List<Value> exprToValueList(Value right) {
        List<Value> newList = new ArrayList<>();
        if (right instanceof JAddExpr) {//+
            JAddExpr jAddExpr = (JAddExpr) right;
            newList.add(jAddExpr.getOp1());
            newList.add(jAddExpr.getOp2());
        } else if (right instanceof JSubExpr) {// -
            JSubExpr jSubExpr = (JSubExpr) right;
            newList.add(jSubExpr.getOp1());
            newList.add(jSubExpr.getOp2());
        } else if (right instanceof JMulExpr) { // *
            JMulExpr jMulExpr = (JMulExpr) right;
            newList.add(jMulExpr.getOp1());
            newList.add(jMulExpr.getOp2());
        } else if (right instanceof JDivExpr) { // /
            JDivExpr jDivExpr = (JDivExpr) right;
            newList.add(jDivExpr.getOp1());
            newList.add(jDivExpr.getOp2());
        } else if (right instanceof JAndExpr) { // &&
            JAndExpr jAndExpr = (JAndExpr) right;
            newList.add(jAndExpr.getOp1());
            newList.add(jAndExpr.getOp2());
        } else if (right instanceof JOrExpr) { // ||
            JOrExpr jOrExpr = (JOrExpr) right;
            newList.add(jOrExpr.getOp1());
            newList.add(jOrExpr.getOp2());
        } else if (right instanceof JXorExpr) { //
            JXorExpr jXorExpr = (JXorExpr) right;
            newList.add(jXorExpr.getOp1());
            newList.add(jXorExpr.getOp2());
        } else if (right instanceof JimpleLocal || right instanceof Constant || right instanceof JInstanceFieldRef || right instanceof StaticFieldRef) {
            newList.add(right);
        }
        return newList;

    }


}
