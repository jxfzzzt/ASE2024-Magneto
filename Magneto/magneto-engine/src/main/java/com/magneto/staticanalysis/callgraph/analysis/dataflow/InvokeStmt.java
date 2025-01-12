package com.magneto.staticanalysis.callgraph.analysis.dataflow;


import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InvokeStmt extends AbstractStmt implements Serializable {

    public static final int SPECIAL_INVOKE = 0;
    public static final int INTERFACE_INVOKE = 1;
    public static final int STATIC_INVOKE = 2;
    public static final int CLASS_NEW = 3;


    public Var invokeVar;
    public List<Var> args;
    public SootMethod sootMethod;
    public transient SootClass sootClass;
    public int invokeType;

    public InvokeStmt(Value invokeValue, List<Value> argsValue, SootMethod sootMethod) {
        if (invokeValue != null) {
            invokeVar = new Var(invokeValue);
        }
        if (argsValue != null) {
            this.args = new ArrayList<>();
            for (Value r : argsValue) {
                Var rr = new Var(r);
                this.args.add(rr);
            }
        }
        this.sootMethod = sootMethod;
    }

    public InvokeStmt(Value invokeValue, List<Value> argsValue, SootClass sootClass) {
        invokeVar = new Var(invokeValue);
        if (argsValue != null) {
            this.args = new ArrayList<>();
            for (Value r : argsValue) {
                Var rr = new Var(r);
                this.args.add(rr);
            }
        }
        this.sootClass = sootClass;
    }

    @Override
    public boolean hasInvokeExp() {
        return true;
    }


    @Override
    public List<Var> getUsedVars() {
        List<Var> result = new ArrayList<>();
        if (!this.invokeVar.isConstant) {
            result.add(this.invokeVar);
        }
        for (Var v : this.args) {
            if (!v.isConstant) {
                result.addAll(this.args);
            }
        }
        return result;
    }

    @Override
    public List<Var> getRightVars() {
        List<Var> result = new ArrayList<>();
        if (!this.invokeVar.isConstant) {
            result.add(this.invokeVar);
        }
        for (Var v : this.args) {
            if (!v.isConstant) {
                result.addAll(this.args);
            }
        }
        return result;

    }
}
