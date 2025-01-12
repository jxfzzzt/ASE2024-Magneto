package com.magneto.util.object;

public class CopyObjectResult<T> {

    private T inputValue;

    private T copyValue;

    public CopyObjectResult() {

    }

    public CopyObjectResult(T inputValue, T copyValue) {
        this.inputValue = inputValue;
        this.copyValue = copyValue;
    }

    public T getCopyValue() {
        return copyValue;
    }

    public T getInputValue() {
        return inputValue;
    }

    public void setInputValue(T inputValue) {
        this.inputValue = inputValue;
    }

    public void setCopyValue(T copyValue) {
        this.copyValue = copyValue;
    }

}
