package com.magneto.asm.traversal;

import soot.SootField;

import java.util.Objects;

public class FieldComponent {

    private String fieldName;

    private String fieldClassName;

    private String fieldDescriptor;

    private String fieldSignature;

    private SootField sootField;

    public FieldComponent(String fieldName, String fieldClassName, String fieldDescriptor, String fieldSignature, SootField sootField) {
        this.fieldName = fieldName;
        this.fieldClassName = fieldClassName;
        this.fieldDescriptor = fieldDescriptor;
        this.fieldSignature = fieldSignature;
        this.sootField = sootField;
    }

    public String getFieldClassName() {
        return fieldClassName;
    }

    public void setFieldClassName(String fieldClassName) {
        this.fieldClassName = fieldClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldDescriptor() {
        return fieldDescriptor;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldDescriptor(String fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    public SootField getSootField() {
        return sootField;
    }

    public String getFieldSignature() {
        return fieldSignature;
    }

    public void setFieldSignature(String fieldSignature) {
        this.fieldSignature = fieldSignature;
    }

    public void setSootField(SootField sootField) {
        this.sootField = sootField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldComponent that = (FieldComponent) o;
        return Objects.equals(fieldSignature, that.fieldSignature);
    }

    @Override
    public int hashCode() {
        return fieldSignature != null ? fieldSignature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FieldComponent{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldSignature='" + fieldSignature + '\'' +
                '}';
    }
}
