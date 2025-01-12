package com.magneto.fuzz.context;

import com.magneto.staticanalysis.MethodType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Context {
    private Class<?> clazz;

    private MethodType methodType;

    private Method method;

    private Constructor constructor;

    private String gptQuery;

    private String gptAnswer;

    private Object object;

    private Object[] paramsValue;

    public Context() {

    }

    public Boolean isMethod() {
        return MethodType.METHOD.equals(methodType);
    }

    public Boolean isConstructor() {
        return MethodType.CONSTRUCTOR.equals(methodType);
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getObject() {
        return object;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public Object[] getParamsValue() {
        return paramsValue;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setParamsValue(Object[] paramsValue) {
        this.paramsValue = paramsValue;
    }

    public String getGptAnswer() {
        return gptAnswer;
    }

    public void setGptAnswer(String gptAnswer) {
        this.gptAnswer = gptAnswer;
    }

    public String getGptQuery() {
        return gptQuery;
    }

    public void setGptQuery(String gptQuery) {
        this.gptQuery = gptQuery;
    }
}
