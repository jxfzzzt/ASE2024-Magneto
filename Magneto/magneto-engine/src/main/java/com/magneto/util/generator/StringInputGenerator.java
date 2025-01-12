package com.magneto.util.generator;

public class StringInputGenerator extends InputGenerator<String> {

    private String getRandomString() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(FORMAT_STR.charAt(randomness.nextInt(FORMAT_STR.length())));
        }
        return sb.toString();
    }

    @Override
    public String generateValue() {
        return getRandomString();
    }

    @Override
    public String generateValue(String minValue, String maxValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] generateOneDimArray() {
        int length = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateOneDimArray(length, null, null);
    }

    @Override
    public String[] generateOneDimArray(int arrayLength, String minValue, String maxValue) {
        String[] array = new String[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = getRandomString();
        }
        return array;
    }

    @Override
    public String[][] generateTwoDimArray() {
        int row = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        int col = randomness.nextInt(MIN_ARRAY_LENGTH, MAX_ARRAY_LENGTH);
        return generateTwoDimArray(row, col, null, null);
    }

    @Override
    public String[][] generateTwoDimArray(int arrRowLength, int arrColLength, String minValue, String maxValue) {
        String[][] array = new String[arrRowLength][arrColLength];
        for (int i = 0; i < arrRowLength; i++) {
            for (int j = 0; j < arrColLength; j++) {
                array[i][j] = getRandomString();
            }
        }
        return array;
    }

}
