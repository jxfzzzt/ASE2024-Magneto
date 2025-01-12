package com.magneto.fuzz.mutate.strategy;

import com.magneto.config.ClientProjectProperty;
import com.magneto.fuzz.mutate.MutationStrategy;
import com.magneto.util.RandomUtil;
import com.magneto.util.ReflectionUtil;
import com.magneto.util.object.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

@Slf4j
public class MutateReferenceStrategy implements MutationStrategy<Object> {
    @Override
    public Object mutate(Class clazz, Object obj) {
        if (Math.random() < SET_NULL_VALUE) {
            return null;
        } else {
            Class inheritClass;
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                Set<Class> classes = ClientProjectProperty.getInheritMap().get(clazz.getName());
                inheritClass = RandomUtil.randomChoose(classes);
            } else {
                inheritClass = clazz;
            }

            if (obj == null) {
                obj = ObjectUtil.newObject(inheritClass);
            }

            if (obj != null) {
                try {
                    for (Field field : obj.getClass().getDeclaredFields()) {
                        Object randomObject = RandomUtil.randomObject(field.getType());
                        ReflectionUtil.setFieldValue(obj, field.getName(), randomObject);
                    }
                } catch (Throwable ignored) {
                }
            }

            return obj;
        }
    }
}
