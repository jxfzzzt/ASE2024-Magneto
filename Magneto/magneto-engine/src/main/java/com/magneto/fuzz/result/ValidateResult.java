package com.magneto.fuzz.result;

public class ValidateResult {

    private boolean success;

    private Throwable validateThrow;

    public ValidateResult() {

    }

    public ValidateResult(boolean success) {
        this.success = success;
        this.validateThrow = null;
    }

    public ValidateResult(boolean success, Throwable validateThrow) {
        this.success = success;
        this.validateThrow = validateThrow;
    }


    public Throwable getValidateThrow() {
        return validateThrow;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setValidateThrow(Throwable validateThrow) {
        this.validateThrow = validateThrow;
    }
}
