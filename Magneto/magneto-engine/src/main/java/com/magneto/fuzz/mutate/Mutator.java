package com.magneto.fuzz.mutate;

import com.magneto.fuzz.FuzzAnalysis;
import com.magneto.fuzz.context.Context;
import com.magneto.fuzz.seed.Seed;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.FieldUtil;
import com.magneto.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@Slf4j
public class Mutator {

    private static final double METHOD_PARAM_VALUE_MUTATE = 0.1;

    private static final double CFG_VALUE_MUTATE = 0.9;

    private static final double UNRELATED_VALUE_MUTATE = 0.6;

    private static final double INHERIT_VALUE_MUTATE = 0.5;

    public static Seed mutateSeed(Seed seed, FuzzAnalysis fuzzAnalysis, Context latestContext) throws Exception {
        Seed newSeed = seed.copy();

        MethodCall targetMethodCall = newSeed.getTargetMethodCall();
        String methodSignature = targetMethodCall.getMethodSignature();

        Object[] paramValues = newSeed.getParamValues();
        Class<?>[] parameterTypes = targetMethodCall.getParameterTypes();

        assertEquals(parameterTypes.length, paramValues.length);

        for (int i = 0; i < paramValues.length; i++) {
            if (fuzzAnalysis.isVariableSource(methodSignature, i)) {
                continue;
            }

            if (fuzzAnalysis.cfgContains(methodSignature, i)) {
                if (Math.random() < CFG_VALUE_MUTATE) {
                    paramValues[i] = MutationFactory.getStrategy(parameterTypes[i]).mutate(parameterTypes[i], paramValues[i]);
                }
            } else if (fuzzAnalysis.methodParamContains(methodSignature, i)) {
                if (Math.random() < METHOD_PARAM_VALUE_MUTATE) {
                    paramValues[i] = null; // just mutate to NULL
                }
            } else {
                if (Math.random() < UNRELATED_VALUE_MUTATE) {
                    paramValues[i] = MutationFactory.getStrategy(parameterTypes[i]).mutate(parameterTypes[i], paramValues[i]);
                }
            }
        }
        newSeed.setParamValues(paramValues);

        Object object = newSeed.getObject();

        Object latestContextObject = latestContext.getObject();
        if (Math.random() < INHERIT_VALUE_MUTATE && latestContextObject != null && object != null
                && object.getClass().isAssignableFrom(latestContextObject.getClass())) {
            object = latestContextObject;
        } else {
            if (object != null) {
                Class<?> clazz = object.getClass();
                for (Field field : clazz.getDeclaredFields()) {
                    Class<?> fieldClass = field.getType();
                    String fieldSignature = FieldUtil.getFieldSignature(clazz, field);

                    if (fuzzAnalysis.isVariableSource(fieldSignature)) {
                        continue;
                    }

                    if (fuzzAnalysis.cfgContains(fieldSignature)) {
                        if (Math.random() < CFG_VALUE_MUTATE) {
                            Object fieldValue = ReflectionUtil.getFieldValue(object, field.getName());
                            ReflectionUtil.setFieldValue(object, field.getName(), MutationFactory.getStrategy(fieldClass).mutate(fieldClass, fieldValue));
                        }
                    } else if (fuzzAnalysis.methodParamContains(fieldSignature)) {
                        if (Math.random() < METHOD_PARAM_VALUE_MUTATE) {
                            ReflectionUtil.setFieldValue(object, field.getName(), null);
                        }
                    } else {
                        if (Math.random() < UNRELATED_VALUE_MUTATE) {
                            Object fieldValue = ReflectionUtil.getFieldValue(object, field.getName());
                            ReflectionUtil.setFieldValue(object, field.getName(), MutationFactory.getStrategy(fieldClass).mutate(fieldClass, fieldValue));
                        }
                    }
                }
            }
        }

        newSeed.setObject(object);

        return newSeed;
    }
}
