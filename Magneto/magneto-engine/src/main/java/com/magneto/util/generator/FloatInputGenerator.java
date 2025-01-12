package com.magneto.util.generator;

public class FloatInputGenerator extends InputGenerator<Float> {
    @Override
    public Float generateValue() {
        return generateValue(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Float generateValue(Float minValue, Float maxValue) {
        assert minValue <= maxValue;
        return randomness.nextFloat(minValue, maxValue);
    }

    @Override
    public Float[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Float[] generateOneDimArray(int arrayLength, Float minValue, Float maxValue) {
        assert minValue <= maxValue;
        Float[] array = new Float[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextFloat(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Float[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public Float[][] generateTwoDimArray(int arrRowLength, int arrColLength, Float minValue, Float maxValue) {
        assert minValue <= maxValue;
        Float[][] array = new Float[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextFloat(minValue, maxValue);
            }
        }
        return array;
    }
}
