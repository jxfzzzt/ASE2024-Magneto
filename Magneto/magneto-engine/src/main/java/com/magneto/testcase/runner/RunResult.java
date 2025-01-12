package com.magneto.testcase.runner;


import com.magneto.instrument.state.StateNode;
import com.magneto.testcase.model.TestcaseUnit;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

@Slf4j
public class RunResult {

    private RunStatus runStatus;

    private Map<String, StateNode> runStateTable;

    private Set<String> runMethodSigSet;

    private TestcaseUnit testcaseUnit;

    public RunResult() {

    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(RunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public Map<String, StateNode> getRunStateTable() {
        return runStateTable;
    }

    public void setRunStateTable(Map<String, StateNode> runStateTable) {
        this.runStateTable = runStateTable;
    }

    public TestcaseUnit getTestcaseUnit() {
        return testcaseUnit;
    }

    public void setTestcaseUnit(TestcaseUnit testcaseUnit) {
        this.testcaseUnit = testcaseUnit;
    }

    public Set<String> getRunMethodSigSet() {
        return runMethodSigSet;
    }

    public void setRunMethodSigSet(Set<String> runMethodSigSet) {
        this.runMethodSigSet = runMethodSigSet;
    }

    @Override
    public String toString() {
        return "RunResult{" +
                "runStatus=" + runStatus +
                ", runStateTable=" + runStateTable +
                ", runMethodSigSet=" + runMethodSigSet +
                ", testcaseUnit=" + testcaseUnit +
                '}';
    }
}
