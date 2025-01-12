package com.magneto.staticanalysis;

import com.magneto.dependency.MavenDependency;
import lombok.NonNull;
import soot.SootMethod;
import soot.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class MethodCall {
    private MavenDependency dependency; // belong to which dependency
    private Class<?> clazz;
    private Method method;
    private Constructor<?> constructor;
    private SootMethod sootMethod;
    private MethodType methodType;

    public MethodCall() {

    }

    public MethodCall(@NonNull MavenDependency dependency, @NonNull Class<?> clazz, @NonNull Method method, @NonNull SootMethod sootMethod) {
        this.dependency = dependency;
        this.clazz = clazz;
        this.method = method;
        this.sootMethod = sootMethod;
        this.methodType = MethodType.METHOD;
    }

    public MethodCall(@NonNull MavenDependency dependency, @NonNull Class<?> clazz, @NonNull Constructor<?> constructor, @NonNull SootMethod sootMethod) {
        this.dependency = dependency;
        this.clazz = clazz;
        this.constructor = constructor;
        this.sootMethod = sootMethod;
        this.methodType = MethodType.CONSTRUCTOR;
    }

    public List<String> getArgTypeNameList() {
        List<Type> parameterTypes = sootMethod.getParameterTypes();
        return parameterTypes.stream().map(Type::toQuotedString).collect(Collectors.toList());
    }

    public Class[] getParameterTypes() {
        if (MethodType.METHOD.equals(methodType)) {
            return method.getParameterTypes();
        } else if (MethodType.CONSTRUCTOR.equals(methodType)) {
            return constructor.getParameterTypes();
        } else {
            throw new RuntimeException("method type error");
        }
    }

    public Boolean isMethod() {
        return MethodType.METHOD.equals(methodType);
    }

    public Boolean isConstructor() {
        return MethodType.CONSTRUCTOR.equals(methodType);
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public void setSootMethod(SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    public MavenDependency getDependency() {
        return dependency;
    }

    public void setDependency(MavenDependency dependency) {
        this.dependency = dependency;
    }

    public String getMethodSignature() {
        return sootMethod.getSignature();
    }

    public String getMethodName() {
        return sootMethod.getName();
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public Boolean isStatic() {
        return sootMethod.isStatic();
    }

    public Boolean isPublic() {
        return sootMethod.isPublic();
    }

    public Integer getArgNum() {
        if (MethodType.METHOD.equals(methodType)) {
            return method.getParameterCount();
        } else if (MethodType.CONSTRUCTOR.equals(methodType)) {
            return constructor.getParameterCount();
        } else {
            throw new RuntimeException("method type error");
        }
    }

    public String getMethodSubSignature() {
        return sootMethod.getSubSignature();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodCall that = (MethodCall) o;
        return getMethodSignature().equals(that.getMethodSignature());
    }

    @Override
    public int hashCode() {
        return getMethodSignature().hashCode();
    }

    @Override
    public String toString() {
        return sootMethod.getSignature();
    }

}
