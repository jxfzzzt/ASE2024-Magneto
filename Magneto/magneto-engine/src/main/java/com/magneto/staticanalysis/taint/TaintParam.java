package com.magneto.staticanalysis.taint;

import soot.SootMethod;

import java.util.HashSet;
import java.util.Objects;

public class TaintParam extends TaintObject {

    private final SootMethod sootMethod;

    private final int index;

    public TaintParam(SootMethod sootMethod, int index, Class<?> clazz, HashSet<String> attributes) {
        super(clazz, attributes, TAINT_PARAMETER);
        this.sootMethod = sootMethod;
        this.index = index;
    }

    public String getMethodSignature() {
        return sootMethod.getSignature();
    }

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaintParam that = (TaintParam) o;

        if (index != that.index) return false;
        return Objects.equals(sootMethod, that.sootMethod);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getClazz() == null) ? 0 : getClazz().hashCode());
        result = prime * result + ((getTaintFlag() == null) ? 0 : getTaintFlag().hashCode());
        result = prime * result + (sootMethod != null ? sootMethod.hashCode() : 0);
        result = prime * result + index;
        return result;
    }

    @Override
    public String toString() {
        return "TaintParam{" +
                "sootMethod=" + sootMethod.getSignature() +
                ", index=" + index +
                '}';
    }
}
