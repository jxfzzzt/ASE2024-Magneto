package com.magneto.util;

import cn.hutool.core.lang.Pair;
import com.magneto.util.generator.*;
import com.magneto.util.object.ObjectUtil;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.Method;
import java.util.*;

public class RandomUtil {

    private static final double EMPTY_CONTAINER = 0.3;

    private static final int MIN_CONTAINER_NUMBER = 1;

    private static final int MAX_CONTAINER_NUMBER = 20;

    private static final Integer MAX_ARRAY_DIM = 2;

    private static final SourceOfRandomness RANDOMNESS = new SourceOfRandomness(new Random());

    public static <T> T randomObject(Class<T> clazz) throws Exception {
        if (clazz == null) {
            return null;
        }

        Pair<String, Integer> parseResult = parseClass(clazz);
        int dimNumber = parseResult.getValue();

        if (dimNumber > MAX_ARRAY_DIM) {
            return null;
        }

        InputGenerator generator = null;
        switch (parseResult.getKey()) {
            case "int":
                generator = new IntegerInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    int[] arr = new int[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (int) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    int[][] arr = new int[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (int) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Integer":
                generator = new IntegerInputGenerator();
                if (dimNumber == 0) return (T) generator.generateValue();
                else if (dimNumber == 1) return (T) generator.generateOneDimArray();
                else if (dimNumber == 2) return (T) generator.generateTwoDimArray();
                break;
            case "long":
                generator = new LongInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    long[] arr = new long[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (long) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    long[][] arr = new long[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (long) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Long":
                generator = new LongInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "float":
                generator = new FloatInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    float[] arr = new float[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (float) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    float[][] arr = new float[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (float) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Float":
                generator = new FloatInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "boolean":
                generator = new BooleanInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    boolean[] arr = new boolean[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (boolean) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    boolean[][] arr = new boolean[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (boolean) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Boolean":
                generator = new BooleanInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "char":
                generator = new CharacterInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    char[] arr = new char[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (char) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    char[][] arr = new char[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (char) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Character":
                generator = new CharacterInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "double":
                generator = new DoubleInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    double[] arr = new double[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (double) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    double[][] arr = new double[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (double) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Double":
                generator = new DoubleInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "byte":
                generator = new ByteInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    byte[] arr = new byte[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (byte) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    byte[][] arr = new byte[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (byte) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Byte":
                generator = new ByteInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "short":
                generator = new ShortInputGenerator();
                if (dimNumber == 0) {
                    return (T) generator.generateValue();
                } else if (dimNumber == 1) {
                    Object[] objects = generator.generateOneDimArray();
                    short[] arr = new short[objects.length];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = (short) objects[i];
                    }
                    return (T) arr;
                } else if (dimNumber == 2) {
                    Object[][] objects = generator.generateTwoDimArray();
                    short[][] arr = new short[objects.length][objects[0].length];
                    for (int i = 0; i < arr.length; i++) {
                        for (int j = 0; j < arr[0].length; j++) {
                            arr[i][j] = (short) objects[i][j];
                        }
                    }
                    return (T) arr;
                }
                break;
            case "java.lang.Short":
                generator = new ShortInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            case "java.lang.String":
            case "java.lang.CharSequence":
                generator = new StringInputGenerator();
                if (parseResult.getValue() == 0) return (T) generator.generateValue();
                else if (parseResult.getValue() == 1) return (T) generator.generateOneDimArray();
                else if (parseResult.getValue() == 2) return (T) generator.generateTwoDimArray();
                break;
            default:
                break;
        }

        if (List.class.isAssignableFrom(clazz)) {
            return randomList(clazz);
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return randomMap(clazz);
        }

        if (Set.class.isAssignableFrom(clazz)) {
            return randomSet(clazz);
        }

        return randomReferenceValue(clazz);
    }

    public static <T> T randomMap(Class<T> clazz) throws Exception {
        if (clazz == null) return null;
        if (!Map.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("the clazz is not the instance of Map");
        }

        T mapInstance = null;
        if (clazz.isInterface()) {
            mapInstance = (T) ObjectUtil.newObject(HashMap.class);
        } else {
            mapInstance = ObjectUtil.newObject(clazz);
        }

        if (mapInstance == null) {
            return null;
        }

        if (Math.random() < EMPTY_CONTAINER) {
            // do nothing
        } else {
            InputGenerator<String> generator = new StringInputGenerator();
            int number = RANDOMNESS.nextInt(MIN_CONTAINER_NUMBER, MAX_CONTAINER_NUMBER);
            Method putMethod = Map.class.getDeclaredMethod("put", Object.class, Object.class);
            for (int i = 0; i < number; i++) {
                putMethod.invoke(mapInstance, generator.generateValue(), generator.generateValue());
            }
        }
        return mapInstance;
    }

    public static <T> T randomList(Class<T> clazz) throws Exception {
        if (clazz == null) return null;
        if (!List.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("the clazz is not the instance of List");
        }

        T listInstance = null;
        if (clazz.isInterface()) {
            listInstance = (T) ObjectUtil.newObject(ArrayList.class);
        } else {
            listInstance = ObjectUtil.newObject(clazz);
        }

        if (listInstance == null) {
            return null;
        }

        if (Math.random() < EMPTY_CONTAINER) {
            // do nothing
        } else {
            InputGenerator<String> generator = new StringInputGenerator();
            int number = RANDOMNESS.nextInt(MIN_CONTAINER_NUMBER, MAX_CONTAINER_NUMBER);
            Method putMethod = List.class.getDeclaredMethod("add", Object.class);
            for (int i = 0; i < number; i++) {
                putMethod.invoke(listInstance, generator.generateValue());
            }
        }
        return listInstance;
    }

    public static <T> T randomSet(Class<T> clazz) throws Exception {
        if (clazz == null) return null;
        if (!Set.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("the clazz is not the instance of Set");
        }

        T setInstance = null;
        if (clazz.isInterface()) {
            setInstance = (T) ObjectUtil.newObject(HashSet.class);
        } else {
            setInstance = ObjectUtil.newObject(clazz);
        }

        if (setInstance == null) {
            return null;
        }

        if (Math.random() < EMPTY_CONTAINER) {
            // do nothing
        } else {
            InputGenerator<String> generator = new StringInputGenerator();
            int number = RANDOMNESS.nextInt(MIN_CONTAINER_NUMBER, MAX_CONTAINER_NUMBER);
            Method putMethod = Set.class.getDeclaredMethod("add", Object.class);
            for (int i = 0; i < number; i++) {
                putMethod.invoke(setInstance, generator.generateValue());
            }
        }
        return setInstance;
    }

    public static <T> T randomReferenceValue(Class<T> clazz) {
        return ObjectUtil.newObject(clazz);
    }

    // key: className, value: dim count
    private static Pair<String, Integer> parseClass(Class<?> clazz) {
        String className = clazz.getTypeName();
        int i = className.indexOf('[');
        if (i == -1) {
            return new Pair<>(className, 0);
        } else {
            int dim = 0;
            for (char c : className.toCharArray()) {
                if (c == '[') dim++;
            }
            return new Pair<>(className.substring(0, i), dim);
        }
    }

    public static <T> T randomChoose(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T randomChoose(Set<T> set) {
        List<T> list = new ArrayList<>(set);
        return randomChoose(list);
    }

    public static <T> T randomChoose(T[] list) {
        if (list == null || list.length == 0) {
            return null;
        }
        Random random = new Random();
        return list[random.nextInt(list.length)];
    }
}
