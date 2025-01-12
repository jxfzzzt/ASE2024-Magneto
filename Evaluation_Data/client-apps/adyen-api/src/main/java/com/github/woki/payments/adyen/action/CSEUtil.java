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
package com.github.woki.payments.adyen.action;

import com.github.woki.payments.adyen.PublicApi;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Locale;

/**
 * @author Willian Oki &lt;willian.oki@gmail.com&gt;
 */
@PublicApi
public final class CSEUtil {
    private static final String CSE_VERSION = "1_0_0";
    private static final String CSE_SEPARATOR = "$";
    private static final String CSE_PREFIX = "payments-adyen-api_";
    private static final SecureRandom CSE_RANDOM = new SecureRandom();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CSEUtil() {
        // utility class
    }

    private static SecretKey aesKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(keySize);
        return kgen.generateKey();
    }

    private static synchronized byte[] iv(SecureRandom random, int ivSize) {
        byte[] iv = new byte[ivSize];
        random.nextBytes(iv);
        return iv;
    }

    static String encrypt(Cipher aesCipher, Cipher rsaCipher, String plainText) throws BadPaddingException, IllegalBlockSizeException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKey aesKey = aesKey(256);
        byte[] iv = iv(CSE_RANDOM, 12);
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));
        byte[] encrypted = aesCipher.doFinal(plainText.getBytes());

        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        byte[] encryptedAESKey;
        try {
            encryptedAESKey = rsaCipher.doFinal(aesKey.getEncoded());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidKeyException(e.getMessage());
        }
        return String.format("%s%s%s%s%s%s", CSE_PREFIX, CSE_VERSION, CSE_SEPARATOR, Base64.encodeBase64String(encryptedAESKey), CSE_SEPARATOR,
                    Base64.encodeBase64String(result));
    }

    public static Cipher aesCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        return Cipher.getInstance("AES/CCM/NoPadding", "BC");
    }

    public static Cipher rsaCipher(@NotNull String cseKeyText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        String[] cseKeyParts = cseKeyText.split("\\|");
        if (cseKeyParts.length != 2) {
            throw new InvalidKeyException("Invalid CSE Key: " + cseKeyText);
        }
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        BigInteger keyComponent1, keyComponent2;
        try {
            keyComponent1 = new BigInteger(cseKeyParts[1].toLowerCase(Locale.getDefault()), 16);
            keyComponent2 = new BigInteger(cseKeyParts[0].toLowerCase(Locale.getDefault()), 16);
        } catch (NumberFormatException e) {
            throw new InvalidKeyException("Invalid CSE Key: " + cseKeyText);
        }
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(keyComponent1, keyComponent2);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        Cipher result = Cipher.getInstance("RSA/None/PKCS1Padding");
        result.init(Cipher.ENCRYPT_MODE, pubKey);
        return result;
    }
}
