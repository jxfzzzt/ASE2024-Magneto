package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;

import java.util.ArrayList;
import java.util.List;

public class AssignmentInvokeStmt extends InvokeStmt {

    public Var leftVar;

    public AssignmentInvokeStmt(Value leftVar, Value base, List<Value> args, SootMethod sootMethod) {
        super(base, args, sootMethod);
        this.leftVar = new Var(leftVar);

    }


    public AssignmentInvokeStmt(Value leftVar, Value base, List<Value> args, SootClass sootClass) {
        super(base, args, sootClass);
        this.leftVar = new Var(leftVar);
        this.invokeType = InvokeStmt.CLASS_NEW;

    }

    public AssignmentInvokeStmt(Value leftVar, Type type, List<Value> args, SootMethod sootMethod) {
        super(null, args, sootMethod);
        this.leftVar = new Var(leftVar);
        this.invokeType = InvokeStmt.STATIC_INVOKE;
        this.sootClass = sootMethod.getDeclaringClass();
    }

    @Override
    public List<Var> getUsedVars() {
        List<Var> result = getRightVars();
        if (!this.leftVar.isConstant) {
            result.add(this.leftVar);
        }

        return result;
    }

    @Override
    public List<Var> getRightVars() {
        List<Var> result = new ArrayList<>();
        if (this.invokeVar != null && !this.invokeVar.isConstant) {
            result.add(this.invokeVar);
        }
        if (this.args != null) {
            for (Var v : this.args) {
                if (!v.isConstant) {
                    result.add(v);
                }
            }
        }
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
