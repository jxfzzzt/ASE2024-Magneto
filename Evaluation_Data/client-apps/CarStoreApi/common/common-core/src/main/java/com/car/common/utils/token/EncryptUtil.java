package com.car.common.utils.token;

/**
 * @author linjiang.xie
 * @date 2020/3/27 15:14
 */
public class EncryptUtil {
    public static String encrypt(String privateKey, String data) throws Exception {
        return RSAEncrypt.encryptByPrivate(data, privateKey);
    }

    public static String decrypt(String publicKey, String data) throws Exception {
        return RSAEncrypt.decryptByPublic(data, publicKey);
    }
}
