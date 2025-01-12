package com.magneto.fuzz.mutate.strategy;

import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutateStringStrategy implements MutationStrategy<String> {

    @Override
    public String mutate(Class clazz, String obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            try {
                return RandomUtil.randomObject(String.class);
            } catch (Exception e) {
                throw new RuntimeException("random string fail", e);
            }
        }
    }

}
