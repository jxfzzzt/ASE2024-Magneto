package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.Var;
import soot.Value;

import java.util.ArrayList;
import java.util.List;

public class TableSwitchStmt extends AbstractStmt {

    public Var switchVar;

    public TableSwitchStmt(Value v) {
        Var var = new Var(v);
        switchVar = var;

    }

    public TableSwitchStmt() {

    }

    @Override
    public List<Var> getUsedVars() {
        if (switchVar == null) {
            return null;
        }
        List<Var> res = new ArrayList<>();
        res.add(switchVar);
        return res;
    }

    @Override
    public List<Var> getRightVars() {
        return getUsedVars();
    }


}
