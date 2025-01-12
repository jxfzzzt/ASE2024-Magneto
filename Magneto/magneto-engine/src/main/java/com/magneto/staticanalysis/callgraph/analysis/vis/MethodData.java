package com.magneto.staticanalysis.callgraph.analysis.vis;


import com.magneto.staticanalysis.callgraph.analysis.CallGraphRange;

import java.util.ArrayList;
import java.util.List;

public class MethodData {

    public boolean isAddedOrDeleteOnGraph;

    public String methodSignature;

    /**
     * METHOD RANGE
     */
    public CallGraphRange range;


    public List<Integer> taintedLines;

    public MethodData(String s) {
        this.methodSignature = s;
        this.taintedLines = new ArrayList<>();
    }

}
