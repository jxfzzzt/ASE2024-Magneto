package com.magneto.staticanalysis.taint;

import soot.Local;
import soot.SootField;
import soot.Unit;

import java.util.HashSet;
import java.util.Objects;

public class FlowAbstraction {

    private final Unit source;

    private final SootField field;

    private Local local;

    private HashSet<String> attributes;

    public FlowAbstraction(Unit source, Local local) {
        this(source, local, (SootField) null);
    }

    public FlowAbstraction(Unit source, SootField field) {
        this(source, (Local) null, field);
    }

    public FlowAbstraction(Unit source, Local local, SootField field) {
        this.source = source;
        this.local = local;
        this.field = field;
        this.attributes = null;
    }

    public FlowAbstraction(Unit source, Local local, SootField field, HashSet<String> attributes) {
        this.source = source;
        this.local = local;
        this.field = field;
        this.attributes = attributes;
    }


    public Unit getSource() {
        return this.source;
    }

    public Local getLocal() {
        return this.local;
    }

    public void setLocal(Local l) {
        this.local = l;
    }

    public SootField getField() {
        return this.field;
    }

    public HashSet<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashSet<String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((local == null) ? 0 : local.hashCode());
        result = prime * result + ((field == null) ? 0 : field.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowAbstraction that = (FlowAbstraction) o;

        if (!Objects.equals(local, that.local)) return false;
        return Objects.equals(field, that.field);
    }

    @Override
    public String toString() {
        if (local != null)
            return "LOCAL " + local;
        if (field != null)
            return "FIELD " + field;
        return "";
    }

}
