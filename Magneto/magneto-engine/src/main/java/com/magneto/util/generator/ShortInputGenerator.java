package com.magneto.util.generator;

public class ShortInputGenerator extends InputGenerator<Short> {
    @Override
    public Short generateValue() {
        return generateValue(Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    public Short generateValue(Short minValue, Short maxValue) {
        assert minValue <= maxValue;
        return randomness.nextShort(minValue, maxValue);

    }

    @Override
    public Short[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    public Short[] generateOneDimArray(int arrayLength, Short minValue, Short maxValue) {
        assert minValue <= maxValue;
        Short[] array = new Short[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextShort(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Short[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    @Override
    public Short[][] generateTwoDimArray(int arrRowLength, int arrColLength, Short minValue, Short maxValue) {
        assert minValue <= maxValue;
        Short[][] array = new Short[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextShort(minValue, maxValue);
            }
        }
        return array;
    }
}
