package com.magneto.util.generator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Random;

public abstract class InputGenerator<T> {
    protected static final int MIN_ARRAY_LENGTH = 0;

    protected static final int MAX_ARRAY_LENGTH = 512;

    protected static final String FORMAT_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789()!@#$%^&*()_+{}?<>,./'\"";

    protected SourceOfRandomness randomness = new SourceOfRandomness(new Random());

    public abstract T generateValue();

    public abstract T generateValue(T minValue, T maxValue);

    public abstract T[] generateOneDimArray();

    public abstract T[] generateOneDimArray(int arrayLength, T minValue, T maxValue);

    public abstract T[][] generateTwoDimArray();

    public abstract T[][] generateTwoDimArray(int arrRowLength, int arrColLength, T minValue, T maxValue);

}
