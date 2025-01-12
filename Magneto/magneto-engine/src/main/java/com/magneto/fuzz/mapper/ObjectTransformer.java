package com.magneto.fuzz.mapper;

@FunctionalInterface
public interface ObjectTransformer {

    Object transform(Object object, Class<?> targetClass) throws Exception;

}
