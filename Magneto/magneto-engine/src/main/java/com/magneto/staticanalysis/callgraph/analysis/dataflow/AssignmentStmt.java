package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.Value;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JimpleLocal;

import java.util.ArrayList;
import java.util.List;

public class AssignmentStmt extends AbstractStmt {

    public static final int LEFT_VALUE_GLOBAL = 0;
    public static final int LEFT_VALUE_LOCAL_FIELD = 1;
    public static final int LEFT_VALUE_LOCAL_VAR = 2;

    public static final int RIGHT_VALUE_LOCAL_VALUE_INIT = 3;


    public Var leftVar;
    public int leftVarType;


    public List<Var> rightVars;
    public int rightVarType;

    public Class assignmentOpt;

    public AssignmentStmt(Value left, List<Value> rightValues) {
        leftVar = new Var(left);
        if (rightValues != null) {
            this.rightVars = new ArrayList<>();
            for (Value r : rightValues) {
                Var rr = new Var(r);
                this.rightVars.add(rr);
            }
            if (this.rightVars.size() == 1) {
                if (this.rightVars.get(0).isConstant) {
                    rightVarType = RIGHT_VALUE_LOCAL_VALUE_INIT;
                }
            }
        }
        if (left instanceof StaticFieldRef) {
            this.leftVarType = AssignmentStmt.LEFT_VALUE_GLOBAL;
        }
        if (left instanceof JimpleLocal) {
            this.leftVarType = AssignmentStmt.LEFT_VALUE_LOCAL_VAR;
        }
        if (left instanceof JInstanceFieldRef) {
            JInstanceFieldRef jifr = (JInstanceFieldRef) left;
            Value v2 = jifr.getBase();
            if (v2 instanceof JimpleLocal) {
                this.leftVarType = AssignmentStmt.LEFT_VALUE_LOCAL_FIELD;
            }
        }
    }

    @Override
    public List<Var> getUsedVars() {
        List<Var> result = getRightVars();
        result.add(this.leftVar);
        return result;
    }

    @Override
    public List<Var> getRightVars() {
        List<Var> result = new ArrayList<>();
        result.addAll(this.rightVars);
        return result;
    }

    @Override
    public boolean isAssignment() {
        return true;
    }

    @Override
    public Var getLeftValue() {
        return this.leftVar;
    }

}
