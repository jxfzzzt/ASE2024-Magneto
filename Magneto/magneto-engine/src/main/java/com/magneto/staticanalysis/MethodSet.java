package com.magneto.staticanalysis;

import com.magneto.util.MethodUtil;
import soot.SootMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodSet {

    private final Set<String> methodSet;

    public MethodSet() {
        this.methodSet = new HashSet<>();
    }

    public MethodSet(Set<String> methodSet) {
        this.methodSet = methodSet;
    }

    public Set<String> getMethodSet() {
        return methodSet;
    }

    public boolean add(String signature) {
        return methodSet.add(signature);
    }

    public boolean add(SootMethod sootMethod) {
        return methodSet.add(sootMethod.getSignature());
    }

    public boolean add(Class clazz, Method method) {
        return methodSet.add(MethodUtil.getMethodSignature(clazz, method));
    }

    public boolean add(Class clazz, Constructor constructor) {
        return methodSet.add(MethodUtil.getMethodSignature(clazz, constructor));
    }

    public boolean containMethod(String signature) {
        return methodSet.contains(signature);
    }

    public boolean containMethod(SootMethod sootMethod) {
        return methodSet.contains(sootMethod.getSignature());
    }

    public boolean containMethod(Class clazz, Method method) {
        return methodSet.contains(MethodUtil.getMethodSignature(clazz, method));
    }

    public boolean containMethod(Class clazz, Constructor constructor) {
        return methodSet.contains(MethodUtil.getMethodSignature(clazz, constructor));
    }


    @Override
    public String toString() {
        return "MethodSet{" +
                "methodSet=" + methodSet +
                '}';
    }
}
