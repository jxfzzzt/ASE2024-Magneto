package com.magneto.util.generator;

public class LongInputGenerator extends InputGenerator<Long> {
    @Override
    public Long generateValue() {
        return generateValue(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public Long generateValue(Long minValue, Long maxValue) {
        assert minValue <= maxValue;
        return randomness.nextLong(minValue, maxValue);
    }

    @Override
    public Long[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public Long[] generateOneDimArray(int arrayLength, Long minValue, Long maxValue) {
        assert minValue <= maxValue;
        Long[] array = new Long[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextLong(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Long[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    @Override
    public Long[][] generateTwoDimArray(int arrRowLength, int arrColLength, Long minValue, Long maxValue) {
        assert minValue <= maxValue;
        Long[][] array = new Long[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextLong(minValue, maxValue);
            }
        }
        return array;
    }
}
