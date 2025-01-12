package com.magneto.staticanalysis.callgraph.analysis.bean;

import com.magneto.staticanalysis.callgraph.analysis.CallGraphRange;
import com.magneto.staticanalysis.callgraph.analysis.init.UnitParser;
import soot.Unit;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitWrapperContainer implements Serializable {

    /**
     * Indicates that these statements, starting from the index, are related to the analysis
     * Diff's call graph = 0
     * Going upwards, the index is not necessarily 0
     */
    private final int startIndex;
    private final List<UnitWrapper> unitWrappers;
    private final Map<Unit, Integer> unitToIdMap;

    private boolean isUnitParsed;

    private CallGraphRange range;

    public UnitWrapperContainer(int startIndex) {
        this.unitWrappers = new ArrayList<>();
        this.startIndex = startIndex;
        this.unitToIdMap = new HashMap<>();
        this.isUnitParsed = false;
    }


    public int getStartIndex() {
        return startIndex;
    }

    public List<UnitWrapper> getUnitWrappers() {
        return unitWrappers;
    }

    public void addUnitToUnitWrapper(int id, Unit unit) {
        UnitWrapper uw = UnitParser.initUnit(unit, id);
        unitWrappers.add(uw);
        unitToIdMap.put(uw.unit, uw.id);
    }

    public int getUnitIdByUnit(Unit unit) {
        return unitToIdMap.get(unit);
    }

    public UnitWrapper getUnitWrapperByUnit(Unit unit) {
        return unitWrappers.get(unitToIdMap.get(unit));
    }

    public boolean isUnitParsed() {
        return isUnitParsed;
    }

    public void setUnitParsed(boolean unitParsed) {
        isUnitParsed = unitParsed;
    }

//    public void accept(ControlUnitsVisitor controlUnitsVisitor){
//        controlUnitsVisitor.doVisit(this);
//    }

    public void resetVisitFlag() {
        for (UnitWrapper uw : this.getUnitWrappers()) {
            uw.isVisited = false;
        }
    }

    public CallGraphRange getRange() {
        return range;
    }

    public void initRange() {
        boolean isFirst = true;
        int start = 0, end = 0;
        for (int i = 0; i < unitWrappers.size(); i++) {
            UnitWrapper uw = unitWrappers.get(i);
            List<Tag> tags = uw.unit.getTags();
            if (tags == null || tags.size() == 0) {
                continue;
            }// 213-165 = 48
            int lineNumber = 0;
            for (Tag tag : tags) {
                if (tag instanceof LineNumberTag) {
                    LineNumberTag lineNumberTag = (LineNumberTag) tag;
                    lineNumber = lineNumberTag.getLineNumber();
                    uw.lineNumber = lineNumber;
                }
            }
            if (isFirst) {
                start = lineNumber;
                isFirst = false;
            }
            if (i == unitWrappers.size() - 1) {
                end = lineNumber;
            }
        }
        if (end != 0) {
            range = new CallGraphRange(start, end);
        }
    }
}
