package com.magneto.staticanalysis.callgraph.global;


public class CallGraphConstant {

    public static final int FORWARD = 1000;
    public static final int BACKWARD = 1002;
    public static final int IS_CONTROL = 1003;
    public static final int SAME = 200;
    public static final int ADD_OR_DELETE = 201;
    public static final int CHANGE = 203;

    public static class VarLinkType {
        public static final int LINK0 = 0;
        public static final int LINK_ADD_REF = 1;
        public static final int LINK_ARITHMETIC = 2;
        public static final int LINK_INVOCATION = 3;
    }


}
