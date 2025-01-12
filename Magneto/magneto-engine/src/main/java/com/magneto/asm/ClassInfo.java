package com.magneto.asm;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassInfo {
    private int version;
    private int access;
    private String name;
    private String signature;
    private String superName;
    private String[] interfaces;
    private List<MethodInfo> methodInfos = new ArrayList<>();
    private List<FieldInfo> fieldInfos = new ArrayList<>();

    public ClassInfo() {

    }

    public ClassInfo(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = interfaces;
    }

    public List<FieldInfo> getStaticFieldInfo() {
        return fieldInfos.stream().filter(fieldInfo -> Modifier.isStatic(fieldInfo.getAccess())).collect(Collectors.toList());
    }

    public List<FieldInfo> getNonStaticFieldInfo() {
        return fieldInfos.stream().filter(fieldInfo -> !Modifier.isStatic(fieldInfo.getAccess())).collect(Collectors.toList());
    }

    public List<MethodInfo> getStaticMethodInfo() {
        return methodInfos.stream().filter(methodInfo -> Modifier.isStatic(methodInfo.getAccess())).collect(Collectors.toList());
    }

    public List<MethodInfo> getNonStaticMethodInfo() {
        return methodInfos.stream().filter(methodInfo -> !Modifier.isStatic(methodInfo.getAccess())).collect(Collectors.toList());
    }

    public void addFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfos.add(fieldInfo);
    }

    public void addMethodInfo(MethodInfo methodInfo) {
        this.methodInfos.add(methodInfo);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public int getAccess() {
        return access;
    }

    public int getVersion() {
        return version;
    }

    public String getSuperName() {
        return superName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }
}
