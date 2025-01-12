package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.Value;

import java.util.ArrayList;
import java.util.List;

public class ThrowStmt extends AbstractStmt {
    public Var var;

    public ThrowStmt(Value v) {
        this.var = new Var(v);
    }


    @Override
    public List<Var> getUsedVars() {
        List<Var> arr = new ArrayList<>();
        arr.add(var);
        return arr;
    }

    @Override
    public List<Var> getRightVars() {
        return null;
    }


}
