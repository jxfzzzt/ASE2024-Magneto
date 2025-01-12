package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutatePrimitiveStrategy implements MutationStrategy<Object> {
    @Override
    public Object mutate(Class clazz, Object obj) {
        assert clazz.isPrimitive();

        // can not assign to NULL
        try {
            return RandomUtil.randomObject(clazz);
        } catch (Exception e) {
            throw new RuntimeException("random primitive mutation failed", e);
        }
    }
}
