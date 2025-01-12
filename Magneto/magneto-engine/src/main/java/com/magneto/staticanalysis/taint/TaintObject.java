package com.magneto.staticanalysis.taint;

import java.util.HashSet;

public abstract class TaintObject {

    protected static final Boolean TAINT_FIELD = Boolean.TRUE;

    protected static final Boolean TAINT_PARAMETER = Boolean.FALSE;

    private final Class<?> clazz;

    private final Boolean taintFlag;

    private final HashSet<String> attributes;

    public TaintObject(Class<?> clazz, HashSet<String> attributes, Boolean taintFlag) {
        this.clazz = clazz;
        this.taintFlag = taintFlag;
        this.attributes = attributes;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Boolean getTaintFlag() {
        return taintFlag;
    }

    public HashSet<String> getAttributes() {
        return attributes;
    }
}
