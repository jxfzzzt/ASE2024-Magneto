package com.magneto.fuzz.result;

import com.magneto.fuzz.seed.Seed;

public class FuzzResult {
    private long consumeTime;

    private Seed seed;

    private boolean success;

    private Throwable validateThrow;

    private Object fuzzTargetObj;

    private Object[] fuzzArgs;

    private InvokeMethodResult invokeResult;

    public FuzzResult() {

    }

    public Throwable getValidateThrow() {
        return validateThrow;
    }

    public void setValidateThrow(Throwable validateThrow) {
        this.validateThrow = validateThrow;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(long consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Object getFuzzTargetObj() {
        return fuzzTargetObj;
    }

    public void setFuzzArgs(Object[] fuzzArgs) {
        this.fuzzArgs = fuzzArgs;
    }

    public Object[] getFuzzArgs() {
        return fuzzArgs;
    }

    public void setFuzzTargetObj(Object fuzzTargetObj) {
        this.fuzzTargetObj = fuzzTargetObj;
    }

    public InvokeMethodResult getInvokeResult() {
        return invokeResult;
    }

    public void setInvokeResult(InvokeMethodResult invokeResult) {
        this.invokeResult = invokeResult;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }
}
