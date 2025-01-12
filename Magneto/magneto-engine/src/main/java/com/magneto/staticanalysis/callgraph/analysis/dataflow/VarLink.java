package com.magneto.staticanalysis.callgraph.analysis.dataflow;


import com.magneto.staticanalysis.callgraph.analysis.bean.Var;

import java.io.Serializable;

public class VarLink implements Serializable {

    Var var;
    int linkType;

    public VarLink(Var var, int linkType) {
        this.var = var;
        this.linkType = linkType;
    }
}
