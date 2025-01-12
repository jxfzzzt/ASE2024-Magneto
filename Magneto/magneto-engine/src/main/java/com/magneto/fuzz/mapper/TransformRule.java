package com.magneto.fuzz.mapper;

import lombok.NonNull;

public class TransformRule {

    private final String initClass;

    private final String targetClass;

    private final ObjectTransformer objectTransformer;

    public TransformRule(@NonNull String initClass, @NonNull String targetClass, @NonNull ObjectTransformer objectTransformer) {
        this.initClass = initClass;
        this.targetClass = targetClass;
        this.objectTransformer = objectTransformer;
    }

    public boolean match(String initClass, String targetClass) {
        boolean initClassMatch = false;
        if ("*".equals(initClass)) {
            initClassMatch = true;
        } else {
            initClassMatch = this.initClass.equals(initClass);
        }

        boolean targetClassMatch = false;
        if ("*".equals(targetClass)) {
            targetClassMatch = true;
        } else {
            targetClassMatch = this.targetClass.equals(targetClass);
        }

        return initClassMatch && targetClassMatch;
    }

    public Object transform(Object object, Class<?> targetClass) throws Exception {
        return objectTransformer.transform(object, targetClass);
    }
}
