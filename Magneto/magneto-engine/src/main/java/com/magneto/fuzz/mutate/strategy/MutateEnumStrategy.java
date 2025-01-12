package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutateEnumStrategy implements MutationStrategy<Enum> {

    @Override
    public Enum mutate(Class clazz, Enum obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            Object[] enumConstants = clazz.getEnumConstants();
            return (Enum) RandomUtil.randomChoose(enumConstants);
        }
    }
}
