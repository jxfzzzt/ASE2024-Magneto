package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.object.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultStrategy implements MutationStrategy<Object> {
    @Override
    public Object mutate(Class clazz, Object obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            try {
                return ObjectUtil.newObject(clazz);
            } catch (Exception e) {
                return obj;
            }
        }
    }
}
