package com.magneto.staticanalysis.callgraph.analysis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NodeIOVar implements Serializable {

    /**
     * index = 0 this
     */
    private List<Var> inputParams;

    private List<Var> outputParams;

    public NodeIOVar() {
        this.inputParams = new ArrayList<>();
        this.outputParams = new ArrayList<>();
    }

    public List<Var> getInputParams() {
        return inputParams;
    }

    public void setInputParams(List<Var> inputParams) {
        this.inputParams = inputParams;
    }

    public List<Var> getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(List<Var> outputParams) {
        this.outputParams = outputParams;
    }

    public void addInputParams(Var inputParam) {
        this.inputParams.add(inputParam);
    }

    public void addOutputParams(Var outputParam) {
        this.outputParams.add(outputParam);
    }

    public List<Var> getChangedReturnVar() {
        List<Var> ret = new ArrayList<>();
        for (Var v : outputParams) {
            if (v.isChange) {
                ret.add(v);
            }
        }
        return ret;
    }

}
