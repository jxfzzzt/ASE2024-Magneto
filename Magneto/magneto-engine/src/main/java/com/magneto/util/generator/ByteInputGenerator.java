package com.magneto.util.generator;

public class ByteInputGenerator extends InputGenerator<Byte> {

    @Override
    public Byte generateValue() {
        return generateValue(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    public Byte generateValue(Byte minValue, Byte maxValue) {
        assert minValue <= maxValue;
        return randomness.nextByte(minValue, maxValue);
    }

    @Override
    public Byte[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    public Byte[] generateOneDimArray(int arrayLength, Byte minValue, Byte maxValue) {
        assert minValue <= maxValue;
        Byte[] array = new Byte[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextByte(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Byte[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    @Override
    public Byte[][] generateTwoDimArray(int arrRowLength, int arrColLength, Byte minValue, Byte maxValue) {
        assert minValue <= maxValue;
        Byte[][] array = new Byte[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextByte(minValue, maxValue);
            }
        }
        return array;
    }

}
