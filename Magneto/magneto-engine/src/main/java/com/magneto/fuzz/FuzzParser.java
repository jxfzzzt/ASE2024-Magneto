package com.magneto.fuzz;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magneto.fuzz.context.Context;
import com.magneto.fuzz.mapper.FuzzObjectMapper;
import com.magneto.fuzz.seed.Seed;
import com.magneto.staticanalysis.MethodCall;
import com.magneto.util.RandomUtil;
import com.magneto.util.ReflectionUtil;
import com.magneto.util.object.ObjectUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

@Slf4j
public class FuzzParser {

    private static final Integer MAX_ARRAY_DIM = 2;

    private static final Pattern REGEX = Pattern.compile(".*<(\\d+)>.*");

    private static final String LEFT_PADDING = "<";

    private static final String RIGHT_PADDING = ">";

    private final ClassLoader loader;

    private final FuzzObjectMapper fuzzObjectMapper;

    public FuzzParser(ClassLoader loader, FuzzObjectMapper fuzzObjectMapper) {
        this.loader = loader;
        this.fuzzObjectMapper = fuzzObjectMapper;
    }

    private Object[] processInputParameters(JSONObject inputParameters, Context context, MethodCall methodCall) {
        Object[] paramsValue = context.getParamsValue();
        Class<?>[] parameterTypes = methodCall.getParameterTypes();
        Object[] ret = new Object[parameterTypes.length];

        try {
            for (int i = 0; i < parameterTypes.length; i++) {
                String key = String.valueOf(i);
                if (inputParameters.containsKey(key)) {
                    JSONObject jsonObject = inputParameters.getJSONObject(key);
                    ret[i] = getValue(jsonObject, parameterTypes[i], paramsValue);
                } else {
                    // if not exist in gpt answer, then assign NULL.
                    ret[i] = null;
                }
            }
        } catch (Exception ignored) {
        }

        return ret;
    }

    private Object processFieldProperties(JSONObject fieldProperties, Context context, MethodCall methodCall) {
        Class<?> clazz = methodCall.getClazz();

        Object object = ObjectUtil.newObject(clazz);

        if (object == null) return null;

        Object[] paramsValue = context.getParamsValue();
        for (Map.Entry<String, Object> entry : fieldProperties.entrySet()) {
            String fieldName = entry.getKey();
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                log.warn("get field by name failed");
                continue;
            }

            if (entry.getValue() instanceof JSONObject) {
                try {
                    JSONObject value = (JSONObject) entry.getValue();
                    Object fieldValue = getValue(value, field.getType(), paramsValue);
                    ReflectionUtil.setFieldValue(object, fieldName, fieldValue);
                } catch (Exception e) {
                    log.warn("setFieldValueError");
                }
            } else {
                log.warn("wrong field properties schema");
            }
        }
        return object;
    }

    // className e.g. int, java.lang.Object, int[], java.lang.Object[], ...
    private Object getValueFromString(String valueStr, Class<?> targetClass, Object[] paramsValue) {
        Matcher matcher = REGEX.matcher(valueStr);
        String group;

        if (matcher.matches()) {
            group = matcher.group(1);
            if (StringUtils.isNumeric(group)) {
                int id = Integer.parseInt(group);
                if (id >= 0 && id < paramsValue.length) {
                    Object o = paramsValue[id];

                    if (o == null) return null;

                    if (String.class.isAssignableFrom(o.getClass())) {
                        String reg = LEFT_PADDING + id + RIGHT_PADDING;
                        return valueStr.replace(reg, (String) o);
                    } else {
                        return fuzzObjectMapper.mapping(o, targetClass);
                    }
                }
            }
        }
        return valueStr;
    }

    private Object getEnumValue(Class<?> clazz, Object value) {
        assertTrue(clazz.isEnum());

        Object[] enumConstants = clazz.getEnumConstants();

        if (value instanceof String) {
            // if `value` is the name of the enum object
            String enumName = (String) value;
            for (Object enumConstant : enumConstants) {
                if (enumConstant instanceof Enum) {
                    Enum enumValue = (Enum) enumConstant;
                    if (enumValue.name().equals(enumName)) {
                        return enumValue;
                    }
                }
            }
        }

        return RandomUtil.randomChoose(enumConstants);
    }

    private boolean checkPrimitiveAssignable(Class exceptedClass, Class targetClass) {
        if (exceptedClass == targetClass) {
            return true;
        }

        if (exceptedClass == int.class && targetClass == Integer.class) {
            return true;
        }

        if (exceptedClass == long.class && targetClass == Long.class) {
            return true;
        }

        if (exceptedClass == float.class && targetClass == Float.class) {
            return true;
        }

        if (exceptedClass == double.class && targetClass == Double.class) {
            return true;
        }

        if (exceptedClass == boolean.class && targetClass == Boolean.class) {
            return true;
        }

        if (exceptedClass == byte.class && targetClass == Byte.class) {
            return true;
        }

        if (exceptedClass == short.class && targetClass == Short.class) {
            return true;
        }

        if (exceptedClass == char.class && targetClass == Character.class) {
            return true;
        }

        return false;
    }


    private Object getValue(JSONObject jsonObject, Class<?> clazz, Object[] paramsValue) throws Exception {
        if (!jsonObject.containsKey("value")) {
            return null;
        }
        Object value = jsonObject.get("value");
        if (value == null) return null;

        if (clazz == null) {
            log.warn("fuzz parser get value warn.");
            return null;
        }

        if (clazz.isEnum()) {
            return getEnumValue(clazz, value);
        }

        Object object = ObjectUtil.newObject(clazz);

        if (value instanceof String) {
            String valueStr = (String) value;
            Object valueFromString = getValueFromString(valueStr, clazz, paramsValue);
            if (valueFromString == null) return null;

            if (clazz.isPrimitive()) {
                if (checkPrimitiveAssignable(clazz, valueFromString.getClass())) {
                    object = valueFromString;
                }
            } else if (clazz.isAssignableFrom(valueFromString.getClass())) {
                object = valueFromString;
            } else if (clazz.isAssignableFrom(String.class)) {
                object = valueStr;
            }
        } else if (value instanceof JSONObject) {
            JSONObject jsonValue = (JSONObject) value;
            if (Map.class.isAssignableFrom(clazz)) {
                // Map
                Method putMethod = Map.class.getDeclaredMethod("put", Object.class, Object.class);
                putMethod.setAccessible(true);
                for (Map.Entry<String, Object> entry : jsonValue.entrySet()) {
                    try {
                        Object keyValue = getValueFromString(entry.getKey(), clazz, paramsValue);
                        Object v = null;
                        if (entry.getValue() instanceof JSONObject) {
                            JSONObject subValue = (JSONObject) entry.getValue();
                            Class<?> loadedClass;
                            try {
                                loadedClass = loader.loadClass(subValue.getStr("type"));
                            } catch (Exception e) {
                                loadedClass = Object.class;
                            }
                            v = getValue(subValue, loadedClass, paramsValue);
                        } else if (entry.getValue() instanceof String) {
                            // need to check
                            v = getValueFromString((String) entry.getValue(), clazz, paramsValue);
                        }

                        if (v != null) {
                            putMethod.invoke(object, keyValue, v);
                        }
                    } catch (Exception ignored) {
                    }
                }
            } else if (Class.class.isAssignableFrom(clazz)) {
                // deal with Class
                String name = jsonValue.getStr("name");
                try {
                    object = loader.loadClass(name);
                } catch (Exception ignored) {
                }
            } else {
                // reference object
                for (Map.Entry<String, Object> entry : jsonValue.entrySet()) {
                    // first get field name
                    String fieldName = entry.getKey();
                    Object valueFromString = getValueFromString(fieldName, clazz, paramsValue);
                    if (String.class.isAssignableFrom(valueFromString.getClass())) {
                        fieldName = (String) valueFromString;
                    }

                    // try to set field value
                    try {
                        Field declaredField = clazz.getDeclaredField(fieldName);
                        Class<?> fieldClass = declaredField.getType();
                        if (entry.getValue() instanceof JSONObject) {
                            Object v = getValue((JSONObject) entry.getValue(), fieldClass, paramsValue);
                            ReflectionUtil.setFieldValue(object, fieldName, v);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        } else if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;

            if (clazz.isArray()) {
                // Array
                object = Array.newInstance(clazz.getComponentType(), jsonArray.size());
                Object[] arr = (Object[]) object;
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object data = jsonArray.get(i);
                    if (data instanceof JSONObject) {
                        JSONObject jsonValue = (JSONObject) data;
                        Object v = getValue(jsonValue, clazz.getComponentType(), paramsValue);
                        arr[i] = v;
                    } else if (data instanceof String) {
                        Object valueFromString = getValueFromString((String) data, Object.class, paramsValue);
                        if (valueFromString != null && clazz.getComponentType().isAssignableFrom(valueFromString.getClass())) {
                            arr[i] = valueFromString;
                        }
                    }
                }
            } else if (List.class.isAssignableFrom(clazz)) {
                // List
                assert object != null;
                Method addMethod = List.class.getDeclaredMethod("add", Object.class);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object data = jsonArray.get(i);
                    if (data instanceof JSONObject) {
                        try {
                            JSONObject jsonValue = (JSONObject) data;
                            Class<?> loadedClass;
                            try {
                                loadedClass = loader.loadClass(jsonValue.getStr("type"));
                            } catch (Exception e) {
                                loadedClass = Object.class;
                            }
                            Object v = getValue(jsonValue, loadedClass, paramsValue);
                            addMethod.invoke(object, v);
                        } catch (Exception ignored) {
                        }
                    } else if (data instanceof String) {
                        Object valueFromString = getValueFromString((String) data, Object.class, paramsValue);
                        if (valueFromString != null) {
                            addMethod.invoke(object, valueFromString);
                        }
                    }
                }
            } else if (Set.class.isAssignableFrom(clazz)) {
                // Set
                assert object != null;
                Method addMethod = Set.class.getDeclaredMethod("add", Object.class);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object data = jsonArray.get(i);
                    if (data instanceof JSONObject) {
                        try {
                            JSONObject jsonValue = (JSONObject) data;
                            Class<?> loadedClass;
                            try {
                                loadedClass = loader.loadClass(jsonValue.getStr("type"));
                            } catch (Exception e) {
                                loadedClass = Object.class;
                            }
                            Object v = getValue(jsonValue, loadedClass, paramsValue);
                            addMethod.invoke(object, v);
                        } catch (Exception ignored) {
                        }
                    } else if (data instanceof String) {
                        Object valueFromString = getValueFromString((String) data, Object.class, paramsValue);
                        if (valueFromString != null) {
                            addMethod.invoke(object, valueFromString);
                        }
                    }
                }
            }
        }

        if (object == null && paramsValue.length == 1) {
            if (paramsValue[0] != null) {
                Object val = fuzzObjectMapper.mapping(paramsValue[0], clazz);
                if (clazz.isAssignableFrom(val.getClass())) {
                    object = val;
                }
            }
        }
        return object;
    }

    private Seed processNullSeed(Context context, MethodCall methodCall) {
        Seed seed = new Seed();
        seed.setTargetMethodCall(methodCall);

        Class<?> clazz = methodCall.getClazz();
        Object[] params = new Object[methodCall.getArgNum()];
        seed.setParamValues(params);

        if (methodCall.isStatic()) {
            seed.setObject(null);
        } else {
            Object o = ObjectUtil.newObject(clazz);
            if (o == null && methodCall.isMethod()) return null;
            seed.setObject(o);
        }
        
        return seed;
    }

    public Seed parseSeed(String gptAnswer, @NonNull Context context, @NonNull MethodCall methodCall) {
        try {
            if (gptAnswer == null) return processNullSeed(context, methodCall);
            Seed seed = new Seed();
            seed.setTargetMethodCall(methodCall);
            JSONObject jsonObject = JSONUtil.parseObj(gptAnswer);
            JSONObject inputParameters = jsonObject.getJSONObject("Arguments"); // gpt default key
            JSONObject fieldProperties = jsonObject.getJSONObject("FieldProperties"); // gpt default key

            if (inputParameters == null && fieldProperties == null) return processNullSeed(context, methodCall);

            Object object = null;
            Object[] params;
            if (Modifier.isStatic(methodCall.isMethod() ? methodCall.getMethod().getModifiers() : methodCall.getConstructor().getModifiers())) {
                // static method, just process the inputParameters
                params = processInputParameters(inputParameters, context, methodCall);
            } else {
                // non-static method, process the inputParameters and fieldProperties
                params = processInputParameters(inputParameters, context, methodCall);
                object = processFieldProperties(fieldProperties, context, methodCall);
                if (methodCall.isMethod()) {
                    if (object == null) return null;
                }
            }

            // to avoid the primitive inputs be null
            Class[] parameterTypes = methodCall.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].isPrimitive() && params[i] == null) {
                    params[i] = RandomUtil.randomObject(parameterTypes[i]);
                }
            }

            seed.setParamValues(params);
            seed.setObject(object);
            return seed;
        } catch (Throwable e) {
            log.warn("ParseSeedError: ", e);
            return null;
        }
    }
}
