package com.magneto.fuzz.result;

import java.util.ArrayList;
import java.util.List;

public class FuzzChainResult {

    private Long fuzzChainTime;

    private Integer failStepNum;

    private Integer successStepNum;

    private Boolean fuzzAllSuccess;

    private final List<FuzzResult> fuzzResultList;

    public FuzzChainResult() {
        this.failStepNum = null;
        this.fuzzResultList = new ArrayList<>();
        this.fuzzAllSuccess = false;
    }

    public Long getFuzzChainTime() {
        return fuzzChainTime;
    }

    public void setFuzzChainTime(Long fuzzChainTime) {
        this.fuzzChainTime = fuzzChainTime;
    }

    public void setSuccessStepNum(Integer successStepNum) {
        this.successStepNum = successStepNum;
    }

    public Integer getSuccessStepNum() {
        return successStepNum;
    }

    public void setFuzzAllSuccess(Boolean fuzzAllSuccess) {
        this.fuzzAllSuccess = fuzzAllSuccess;
    }

    public void addFuzzResult(FuzzResult fuzzResult) {
        this.fuzzResultList.add(fuzzResult);
    }

    public void setFailStepNum(Integer failStepNum) {
        this.failStepNum = failStepNum;
    }

    public Boolean getFuzzAllSuccess() {
        return fuzzAllSuccess;
    }

    public Integer getFailStepNum() {
        return failStepNum;
    }

    public List<FuzzResult> getFuzzResultList() {
        return fuzzResultList;
    }

}
