package com.magneto.asm;

public class FieldInfo {
    private int access;
    private String name;
    private String descriptor;
    private String signature;
    private Object value;

    public FieldInfo() {

    }

    public FieldInfo(int access, String name, String descriptor, String signature, Object value) {
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.value = value;
    }

    public int getAccess() {
        return access;
    }

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
