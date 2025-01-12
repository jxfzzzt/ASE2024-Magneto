package com.magneto.fuzz.mapper;

import com.magneto.fuzz.mapper.mock.MockHashSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

@Slf4j
public class FuzzObjectMapper {

    private final List<TransformRule> FUZZ_MAPPING_RULES = new ArrayList<>();

    private final ClassLoader classLoader;

    public FuzzObjectMapper(ClassLoader classLoader) {
        this.classLoader = classLoader;
        init();
    }

    public Object mapping(Object object, @NonNull Class<?> targetClass) {
        if (object == null) {
            return null;
        }

        if (object.getClass().getName().equals(targetClass.getName())) {
            return object;
        }

        for (TransformRule rule : FUZZ_MAPPING_RULES) {
            if (rule.match(object.getClass().getName(), targetClass.getName())) {
                try {
                    return rule.transform(object, targetClass);
                } catch (Exception e) {
                    log.warn("mapping object occur exception: ", e);
                }
            }
        }

        return object;
    }

    private void init() {
        FUZZ_MAPPING_RULES.add(new TransformRule(String.class.getName(), File.class.getName(), ((object, targetClass) -> new File((String) object))));

        FUZZ_MAPPING_RULES.add(new TransformRule(File.class.getName(), String.class.getName(), ((object, targetClass) -> {
            File file = (File) object;
            return file.getAbsolutePath();
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(String.class.getName(), URI.class.getName(), ((object, targetClass) -> new URI((String) object))));

        FUZZ_MAPPING_RULES.add(new TransformRule(URI.class.getName(), String.class.getName(), ((object, targetClass) -> object.toString())));

        FUZZ_MAPPING_RULES.add(new TransformRule(URI.class.getName(), "org.apache.http.client.methods.HttpUriRequest", ((object, targetClass) -> {
            Class<?> httpGetClass = classLoader.loadClass("org.apache.http.client.methods.HttpGet");
            Constructor<?> declaredConstructor = httpGetClass.getDeclaredConstructor(URI.class);
            return declaredConstructor.newInstance((URI) object);
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule("org.apache.http.client.methods.HttpGet", URI.class.getName(), ((object, targetClass) -> {
            Class<?> httpGetClass = classLoader.loadClass("org.apache.http.client.methods.HttpRequestBase");
            Method declaredMethod = httpGetClass.getDeclaredMethod("getURI");
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(object);
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule("org.apache.http.client.methods.HttpGet", String.class.getName(), ((object, targetClass) -> {
            Class<?> httpGetClass = classLoader.loadClass("org.apache.http.client.methods.HttpRequestBase");
            Method declaredMethod = httpGetClass.getDeclaredMethod("getURI");
            declaredMethod.setAccessible(true);
            URI uri = (URI) declaredMethod.invoke(object);
            return uri.toString();
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(String.class.getName(), "org.apache.http.client.methods.HttpUriRequest", ((object, targetClass) -> {
            URI uri = new URI((String) object);
            Class<?> httpGetClass = classLoader.loadClass("org.apache.http.client.methods.HttpGet");
            Constructor<?> declaredConstructor = httpGetClass.getDeclaredConstructor(URI.class);
            return declaredConstructor.newInstance(uri);
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(String.class.getName(), Class.class.getName(), ((object, targetClass) -> {
            try {
                return classLoader.loadClass((String) object);
            } catch (Exception e) {
                return null;
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(Class.class.getName(), String.class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                return ((Class<?>) object).getName();
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(int[].class.getName(), HashSet.class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                int[] array = (int[]) object;

                return new MockHashSet<Integer>(array.length);
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule(HashSet.class.getName(), int[].class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                HashSet hashSet = (HashSet) object;
                return new int[hashSet.size()];
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule("*", List.class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                List list = new ArrayList();
                list.add(object);
                return list;
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule("*", Set.class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                HashSet hashSet = new HashSet();
                hashSet.add(object);
                return hashSet;
            }
        })));

        FUZZ_MAPPING_RULES.add(new TransformRule("*", Collection.class.getName(), ((object, targetClass) -> {
            if (object == null) {
                return null;
            } else {
                List list = new ArrayList();
                list.add(object);
                return list;
            }
        })));
    }

}
