package com.magneto.fuzz.result;

import java.util.Objects;

public class InvokeMethodResult {

    private Object[] inputParams;

    private ResultStatus resultStatus;

    private Throwable throwValue;

    private Object returnValue;


    public InvokeMethodResult() {
    }

    public Object[] getInputParams() {
        return inputParams;
    }

    public void setInputParams(Object[] inputParams) {
        this.inputParams = inputParams;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Throwable getThrowValue() {
        return throwValue;
    }

    public void setThrowValue(Throwable throwValue) {
        this.throwValue = throwValue;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        InvokeMethodResult that = (InvokeMethodResult) object;

        if (resultStatus != that.resultStatus) return false;
        if (!Objects.equals(throwValue, that.throwValue)) return false;
        return Objects.equals(returnValue, that.returnValue);
    }

    @Override
    public int hashCode() {
        int result = resultStatus != null ? resultStatus.hashCode() : 0;
        result = 31 * result + (throwValue != null ? throwValue.hashCode() : 0);
        result = 31 * result + (returnValue != null ? returnValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InvokeMethodResult{" +
                "resultStatus=" + resultStatus +
                ", throwValue=" + throwValue +
                ", returnValue=" + returnValue +
                '}';
    }
}
