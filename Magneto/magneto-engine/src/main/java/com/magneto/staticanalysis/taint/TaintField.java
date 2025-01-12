package com.magneto.staticanalysis.taint;

import com.magneto.util.FieldUtil;
import soot.SootField;

import java.util.HashSet;
import java.util.Objects;

public class TaintField extends TaintObject {

    private final SootField sootField;

    public TaintField(SootField sootField, Class<?> clazz, HashSet<String> attributes) {
        super(clazz, attributes, TAINT_FIELD);
        this.sootField = sootField;
    }

    public SootField getSootField() {
        return sootField;
    }

    public String getFieldSignature() {
        return FieldUtil.getFieldSignature(sootField);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getClazz() == null) ? 0 : getClazz().hashCode());
        result = prime * result + ((getTaintFlag() == null) ? 0 : getTaintFlag().hashCode());
        result = prime * result + ((sootField == null) ? 0 : sootField.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaintField that = (TaintField) o;

        return Objects.equals(sootField, that.sootField);
    }

    @Override
    public String toString() {
        return "TaintField{" +
                "sootField=" + sootField.getSignature() +
                '}';
    }
}
