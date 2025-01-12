package com.magneto.asm.traversal;

import soot.SootMethod;
import soot.Type;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodComponent {

    private String methodName;

    private String methodClassName;

    private String methodDescriptor;

    private String methodSignature;

    private SootMethod sootMethod;

    private boolean lambda;

    public MethodComponent(String methodName, String methodClassName, String methodDescriptor, String methodSignature, SootMethod sootMethod) {
        this.methodName = methodName;
        this.methodClassName = methodClassName;
        this.methodDescriptor = methodDescriptor;
        this.methodSignature = methodSignature;
        this.sootMethod = sootMethod;
        this.lambda = this.methodName.contains("$");
    }

    public List<String> getArgTypeNameList() {
        if (sootMethod == null) {
            return null;
        }
        List<Type> parameterTypes = sootMethod.getParameterTypes();
        return parameterTypes.stream().map(Type::toQuotedString).collect(Collectors.toList());
    }

    public void setMethodDescriptor(String methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setSootMethod(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodClassName() {
        return methodClassName;
    }

    public void setMethodClassName(String methodClassName) {
        this.methodClassName = methodClassName;
    }

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public boolean isLambda() {
        return lambda;
    }

    public void setLambda(boolean lambda) {
        this.lambda = lambda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodComponent that = (MethodComponent) o;
        return Objects.equals(methodSignature, that.methodSignature);
    }

    @Override
    public int hashCode() {
        return methodSignature != null ? methodSignature.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MethodComponent{" +
                "methodName='" + methodName + '\'' +
                ", methodSignature='" + methodSignature + '\'' +
                '}';
    }
}
