package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.Value;

import java.util.ArrayList;
import java.util.List;

public class ParameterStmt extends AssignmentStmt {

    public ParameterStmt(Value v) {
        super(v, null);
    }


    @Override
    public List<Var> getUsedVars() {
        List<Var> result = new ArrayList<>();
        result.add(this.leftVar);
        return result;
    }

}
