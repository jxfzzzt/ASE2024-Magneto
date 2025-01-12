package com.magneto.fuzz.mutate;

@FunctionalInterface
public interface MutationStrategy<T> {

    double SET_NULL_VALUE = 0.1;

    T mutate(Class<T> clazz, T obj);

}
