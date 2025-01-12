package com.magneto.staticanalysis.callgraph.analysis.dataflow;

import com.magneto.staticanalysis.callgraph.analysis.bean.UnitWrapper;

public class AdditionalEdge {

    UnitWrapper a;
    UnitWrapper b;

    public AdditionalEdge(UnitWrapper a, UnitWrapper b) {
        this.a = a;
        this.b = b;

    }
}
