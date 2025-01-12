package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutateArrayStrategy implements MutationStrategy<Object> {

    @Override
    public Object mutate(Class clazz, Object obj) {
        assert clazz.isArray();

        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            int arrayDimension = getArrayDimension(clazz);
            if (arrayDimension <= 2) {
                try {
                    return RandomUtil.randomObject(clazz);
                } catch (Exception e) {
                    log.warn("class: {}, mutate array fail", clazz.getName());
                    return obj;
                }
            } else {
                return obj;
            }
        }
    }

    private static int getArrayDimension(Class<?> clazz) {
        String className = clazz.getName();
        int dimension = 0;
        for (int i = 0; i < className.length() && className.charAt(i) == '['; i++) {
            dimension++;
        }
        return dimension;
    }
}
