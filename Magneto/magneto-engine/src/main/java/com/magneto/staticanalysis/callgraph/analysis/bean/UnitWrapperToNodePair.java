package com.magneto.staticanalysis.callgraph.analysis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UnitWrapperToNodePair implements Serializable {

    /**
     * parent :
     * unit a.b(d,"ccc");  r4
     */
    private final UnitWrapper fromInvokeStmt;
    /**
     * class A{
     * public int b(Param param,String d){
     * return param.v+d;
     * }
     * }
     */
    private final Node toInvokedMethod;
    //
//    private List<Var> fromParam;
//    //Different from `NodeIo`: `NodeIo`'s inputParam refers to the method's input parameters,
//    while `fromParam` refers to the Jimple format parameters passed to the called method

//    private List<Var> toParam;   // Retrieve the inputParam from the called method
    private final List<VarPair> returnValuePair;
    private final List<VarPair> baseAndArgsPair;

    public UnitWrapperToNodePair(UnitWrapper uw, Node node) {
        this.fromInvokeStmt = uw;
        this.toInvokedMethod = node;
//        this.fromParam = new ArrayList<>();
//        this.toParam = new ArrayList<>();
        this.baseAndArgsPair = new ArrayList<>();
        this.returnValuePair = new ArrayList<>();
//        initValuePair();
    }

    private void initValuePair() {
        // UnitWrapper -> Node
    }

    public Node getToInvokedMethod() {
        return toInvokedMethod;
    }

//    public List<VarPair> getParamPair() {
//        return paramPair;
//    }

//    public VarPair getInstancePair() {
//        return instancePair;
//    }

    public List<VarPair> getReturnValuePair() {
        return returnValuePair;
    }

//    public List<Var> getFromParam() {
//        return fromParam;
//    }
//
//    public List<Var> getToParam() {
//        return toParam;
//    }


    public List<VarPair> getBaseAndArgsPair() {
        return baseAndArgsPair;
    }


    public UnitWrapper getFromInvokeStmt() {
        return fromInvokeStmt;
    }
}
