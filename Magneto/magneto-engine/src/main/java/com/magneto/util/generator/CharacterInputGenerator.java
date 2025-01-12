package com.magneto.util.generator;

public class CharacterInputGenerator extends InputGenerator<Character> {

    @Override
    public Character generateValue() {
        return generateValue(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    @Override
    public Character generateValue(Character minValue, Character maxValue) {
        assert minValue <= maxValue;
        return randomness.nextChar(minValue, maxValue);
    }

    @Override
    public Character[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, Character.MIN_VALUE, Character.MAX_VALUE);
    }

    @Override
    public Character[] generateOneDimArray(int arrayLength, Character minValue, Character maxValue) {
        assert minValue <= maxValue;
        Character[] array = new Character[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = randomness.nextChar(minValue, maxValue);
        }
        return array;
    }

    @Override
    public Character[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, Character.MIN_VALUE, Character.MAX_VALUE);
    }

    @Override
    public Character[][] generateTwoDimArray(int arrRowLength, int arrColLength, Character minValue, Character maxValue) {
        assert minValue <= maxValue;
        Character[][] array = new Character[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = randomness.nextChar(minValue, maxValue);
            }
        }
        return array;
    }
}
