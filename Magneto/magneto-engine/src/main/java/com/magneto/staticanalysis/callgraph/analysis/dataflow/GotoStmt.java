package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;

import java.util.List;

public class GotoStmt extends AbstractStmt {

    public int gotoUnitId;

    public GotoStmt(int id) {
        this.gotoUnitId = id;
    }


    @Override
    public List<Var> getUsedVars() {
        return null;
    }

    @Override
    public List<Var> getRightVars() {
        return null;
    }

}
