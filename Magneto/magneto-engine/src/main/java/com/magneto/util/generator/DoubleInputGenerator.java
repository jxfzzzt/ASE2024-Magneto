package com.magneto.util.generator;

public class DoubleInputGenerator extends InputGenerator<Double> {
    @Override
    public Double generateValue() {
        return generateValue(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Double generateValue(Double minValue, Double maxValue) {
        assert minValue <= maxValue;
        return randomness.nextDouble(minValue, maxValue);
    }

    @Override
    public Double[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Double[] generateOneDimArray(int arrayLength, Double minValue, Double maxValue) {
        assert minValue <= maxValue;
        Double[] array = new Double[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextDouble(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Double[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Double[][] generateTwoDimArray(int arrRowLength, int arrColLength, Double minValue, Double maxValue) {
        assert minValue <= maxValue;
        Double[][] array = new Double[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextDouble(minValue, maxValue);
            }
        }
        return array;
    }
}
