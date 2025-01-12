/*
 * Copyright 2015 Willian Oki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.github.woki.payments.adyen;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
public class ToStringStyle extends org.apache.commons.lang3.builder.ToStringStyle {
    private static final int CARD_MIN_LEN = 14;
    private static final int CARD_BIN_LEN = 6;
    private static final int CARD_SUFFIX_LEN = 4;
    private static final int CARD_UNMASKED_LEN = CARD_BIN_LEN + CARD_SUFFIX_LEN;
    private static final char CARD_MASK_CHAR = '*';

    public static final ToStringStyle DEFAULT_STYLE = new ToStringStyle();

    public ToStringStyle() {
        super();
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        switch (fieldName) {
            case "cardNumber":
                buffer.append(maskCardNumber((String) value));
                break;
            case "cvc":
                String original = (String) value;
                buffer.append(extractMask(original.length(), 0, CARD_MASK_CHAR));
                break;
            default:
                buffer.append(value);
        }
    }

    private static String maskCardNumber(String number) {
        if (number == null || number.length() < CARD_MIN_LEN) {
            return "";
        }
        return extractCardBin(number) + extractMask(number.length(), CARD_UNMASKED_LEN, CARD_MASK_CHAR) + extractCardSuffix(number);
    }

    private static String extractCardBin(String number) {
        if (number != null && number.length() >= CARD_MIN_LEN) {
            return number.substring(0, CARD_BIN_LEN);
        }
        return "";
    }

    private static String extractCardSuffix(String number) {
        if (number != null && number.length() >= CARD_MIN_LEN) {
            return number.substring(number.length() - CARD_SUFFIX_LEN);
        }
        return "";
    }

    private static String extractMask(int originalLength, int unmaskedLength, char mask) {
        int maskLength = originalLength - unmaskedLength;
        if (maskLength <= 0) {
            return "";
        }

        char[] buff = new char[maskLength];
        while (maskLength > 0) {
            maskLength--;
            buff[maskLength] = mask;
        }
        return new String(buff);
    }
}
