package com.magneto.asm;

public class MethodInfo {
    private int access;
    private String name;
    private String descriptor;
    private String signature;
    private String[] exceptions;

    public MethodInfo() {

    }

    public MethodInfo(int access, String name, String descriptor, String signature, String[] exceptions) {
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
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

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    public String getSignature() {
        return signature;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getName() {
        return name;
    }

    public int getAccess() {
        return access;
    }

    public String[] getExceptions() {
        return exceptions;
    }
}
