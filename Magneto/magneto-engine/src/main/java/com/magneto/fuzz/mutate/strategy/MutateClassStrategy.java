package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MutateClassStrategy implements MutationStrategy<Class> {

    private static final Class<?>[] CLASS_ARRAY = new Class[]
            {String.class, Integer.class, Double.class, Float.class, Long.class, Boolean.class, Number.class, File.class};

    @Override
    public Class mutate(Class clazz, Class obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            return RandomUtil.randomChoose(CLASS_ARRAY);
        }
    }

}
