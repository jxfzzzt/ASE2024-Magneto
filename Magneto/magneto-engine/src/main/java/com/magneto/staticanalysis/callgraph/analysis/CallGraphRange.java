package com.magneto.staticanalysis.callgraph.analysis;

import java.io.Serializable;

public class CallGraphRange implements Serializable {

    /**
     * type insert/delete
     * ChangeEntityDesc.StageITreeType.PREV_TREE_NODE
     */

    public int startLineNo;
    public int endLineNo;

    public CallGraphRange(int start, int end) {
        this.startLineNo = start;
        this.endLineNo = end;
    }


    @Override
    public String toString() {
        return "(" + this.startLineNo + "," + this.endLineNo + ")";
    }


}
