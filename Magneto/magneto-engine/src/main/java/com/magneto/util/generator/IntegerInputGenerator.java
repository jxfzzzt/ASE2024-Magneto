package com.magneto.util.generator;

public class IntegerInputGenerator extends InputGenerator<Integer> {

    @Override
    public Integer generateValue() {
        return generateValue(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Integer generateValue(Integer minValue, Integer maxValue) {
        assert minValue <= maxValue;
        return randomness.nextInt(minValue, maxValue);
    }

    @Override
    public Integer[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Integer[] generateOneDimArray(int arrayLength, Integer minValue, Integer maxValue) {
        assert minValue <= maxValue;
        Integer[] array = new Integer[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextInt(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Integer[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public Integer[][] generateTwoDimArray(int arrRowLength, int arrColLength, Integer minValue, Integer maxValue) {
        assert minValue <= maxValue;
        Integer[][] array = new Integer[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextInt(minValue, maxValue);
            }
        }
        return array;
    }

}
