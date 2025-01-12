package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.Value;

import java.util.ArrayList;
import java.util.List;

public class ReturnStmt extends AbstractStmt {

    public boolean isVoid;
    public Var var;

    public ReturnStmt(Value v) {
        if (v != null) {
            this.var = new Var(v);
            isVoid = false;
        } else {
            isVoid = true;
        }
    }

    @Override
    public List<Var> getUsedVars() {
        if (isVoid) {
            return null;
        }
        List<Var> result = new ArrayList<>();
        if (!this.var.isConstant) {
            result.add(this.var);
        }
        return result;
    }

    @Override
    public List<Var> getRightVars() {
        return null;
    }
}
