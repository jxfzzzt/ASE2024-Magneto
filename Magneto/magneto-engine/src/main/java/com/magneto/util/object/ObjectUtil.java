package com.magneto.util.object;

import cn.hutool.core.io.FileUtil;
import com.magneto.util.RandomUtil;
import com.magneto.util.ReflectionUtil;
import lombok.NonNull;
import org.apache.commons.lang3.SerializationUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisSerializer;
import org.objenesis.ObjenesisStd;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ObjectUtil {

    public static <T> T newObject(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }

        T o = null;
        try {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                int parameterCount = constructor.getParameterCount();
                try {
                    o = (T) constructor.newInstance(new Object[parameterCount]);
                    return o;
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable ignored) {
        }

        if (clazz.isEnum()) {
            return RandomUtil.randomChoose(clazz.getEnumConstants());
        }

        if (clazz == File.class) {
            return (T) FileUtil.createTempFile();
        }

        if (Map.class.isAssignableFrom(clazz)) {
            if (clazz.isInterface()) {
                return (T) new HashMap<>();
            }
        }

        if (Set.class.isAssignableFrom(clazz)) {
            if (clazz.isInterface()) {
                return (T) new HashSet<>();
            }
        }

        if (List.class.isAssignableFrom(clazz)) {
            if (clazz.isInterface()) {
                return (T) new ArrayList<>();
            }
        }

        // iff can not be instanced by constructor
        Objenesis objenesis;
        boolean getObject = false;
        try {
            objenesis = new ObjenesisStd();
            o = objenesis.newInstance(clazz);
            getObject = true;
        } catch (Throwable ignored) {
        }

        if (getObject) {
            return o;
        }

        try {
            objenesis = new ObjenesisSerializer();
            o = objenesis.newInstance(clazz);
        } catch (Throwable ignored) {
        }

        return o;
    }

    public static void copyObjectField(Object dest, Object src) {
        if (dest == null || src == null) return;

        if (!dest.getClass().getName().equals(src.getClass().getName())) return;

        Field[] declaredFields = dest.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isFinal(field.getModifiers())) continue;
            try {
                Object fieldValue = ReflectionUtil.getFieldValue(src, field.getName());
                ReflectionUtil.setFieldValue(dest, field.getName(), fieldValue);
            } catch (Exception ignored) {
            }
        }
    }

    public static <T> CopyObjectResult<T> copyObject(T object) {
        if (object == null) {
            return new CopyObjectResult<>(null, null);
        }

        // handle Object.class
        if (Object.class.equals(object.getClass())) {
            return new CopyObjectResult<>(object, object);
        }

        // handle File.class
        if (File.class.equals(object.getClass())) {
            String filePath = ((File) object).getAbsolutePath();
            File newFile = new File(filePath);
            return (CopyObjectResult<T>) new CopyObjectResult<>(object, newFile);
        }

        // handle InputStream.class
        if (object instanceof InputStream) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = (InputStream) object;

            byte[] buffer = new byte[1024];
            int length;

            try {
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception ignored) {
            }

            byte[] byteArray = outputStream.toByteArray();
            return (CopyObjectResult<T>) new CopyObjectResult<>(new ByteArrayInputStream(byteArray), new ByteArrayInputStream(byteArray));
        }

        if (checkIsCloneable(object.getClass())) {
            try {
                Class<?> clazz = object.getClass();
                Method cloneMethod = clazz.getDeclaredMethod("clone");
                Object cloneObject = cloneMethod.invoke(object);
                return (CopyObjectResult<T>) new CopyObjectResult<>(object, cloneObject);
            } catch (Exception ignored) {
            }
        }

        if (checkIsSerializable(object.getClass())) {
            try {
                Serializable s = (Serializable) object;
                Serializable newValue = SerializationUtils.clone(s);
                return (CopyObjectResult<T>) new CopyObjectResult<>(object, newValue);
            } catch (Exception ignored) {
            }
        }

        return new CopyObjectResult<>(object, object);
    }

    private static boolean checkIsSerializable(@NonNull Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        List<Class<?>> superClassInterfaces = Arrays.asList(superclass.getInterfaces());
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> inter : interfaces) {
            if (!superClassInterfaces.contains(inter) && Serializable.class.equals(inter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIsCloneable(@NonNull Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        List<Class<?>> superClassInterfaces = Arrays.asList(superclass.getInterfaces());
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> inter : interfaces) {
            if (!superClassInterfaces.contains(inter) && Cloneable.class.equals(inter)) {
                return true;
            }
        }
        return false;
    }

}
