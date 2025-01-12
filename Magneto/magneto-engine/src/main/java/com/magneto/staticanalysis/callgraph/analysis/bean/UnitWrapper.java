package com.magneto.staticanalysis.callgraph.analysis.bean;


import com.magneto.staticanalysis.callgraph.analysis.dataflow.AbstractStmt;
import soot.Unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitWrapper implements Serializable {

    public int id;

    public Unit unit;

    public AbstractStmt parsedStmt;

    public List<Var> before;

    public List<Var> after;

    public List<UnitWrapper> predecessorUnitWrapper;
    public List<UnitWrapper> successorUnitWrapper;
    public boolean isChange;
    public boolean isVisited;
    public int lineNumber;
    private Map<Var, UnitWrapper> directlyConnectedUnitWrapper;
    private int assignmentType;

    public UnitWrapper(Unit unit, int id) {
        this.unit = unit;
        this.id = id;
        this.before = new ArrayList<>();
        this.after = new ArrayList<>();
        this.successorUnitWrapper = new ArrayList<>();
        this.predecessorUnitWrapper = new ArrayList<>();
        this.isChange = false;
        this.directlyConnectedUnitWrapper = new HashMap<>();
        isVisited = false;
        assignmentType = -1;
    }


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.successorUnitWrapper.size() != 0) {
            for (UnitWrapper uw : this.successorUnitWrapper) {
                stringBuilder.append(uw.id);
                stringBuilder.append(",");
            }
        }
        String unitStr = unit == null ? "null" : unit.toString();
        if (isChange) {
            return "+" + id + " " + unitStr + " " + stringBuilder;
        }
        return id + " " + unitStr + " " + stringBuilder;
    }
//
//    public Map<Var, UnitWrapper> getDirectlyConnectedUnitWrapper() {
//        return directlyConnectedUnitWrapper;
//    }

    public void setDirectlyConnectedUnitWrapper(Map<Var, UnitWrapper> directlyConnectedUnitWrapper) {
        this.directlyConnectedUnitWrapper = directlyConnectedUnitWrapper;
    }

    public int getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(int assignmentType) {
        this.assignmentType = assignmentType;
    }
}
