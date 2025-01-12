package com.magneto.staticanalysis.callgraph.analysis.bean;

import java.io.Serializable;

public class VarPair implements Serializable {

    public Var from;
    public Var to;

    public VarPair(Var from, Var to) {
        this.from = from;
        this.to = to;
    }
}
