package com.magneto.util.generator;

public class BooleanInputGenerator extends InputGenerator<Boolean> {

    @Override
    public Boolean generateValue() {
        return randomness.nextBoolean();
    }

    @Override
    public Boolean generateValue(Boolean minValue, Boolean maxValue) {
        return randomness.nextBoolean();
    }


    @Override
    public Boolean[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Boolean.FALSE, Boolean.TRUE);
    }

    @Override
    public Boolean[] generateOneDimArray(int arrLength, Boolean minValue, Boolean maxValue) {
        Boolean[] array = new Boolean[arrLength];
        for (int i = 0; i < arrLength; i++) {
            array[i] = randomness.nextBoolean();
        }
        return array;
    }

    @Override
    public Boolean[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Boolean.FALSE, Boolean.TRUE);
    }

    @Override
    public Boolean[][] generateTwoDimArray(int arrRowLength, int arrColLength, Boolean minValue, Boolean maxValue) {
        Boolean[][] array = new Boolean[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextBoolean();
            }
        }
        return array;
    }

}
