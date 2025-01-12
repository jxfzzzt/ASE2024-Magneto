package com.car.common.utils.token;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author linjiang.xie
 * @date 2020/3/27 15:14
 */
public class RSAEncrypt {
    public static void genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
        System.out.println(publicKeyString);
        System.out.println(privateKeyString);
    }

    public static String encryptByPublic(String str, String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return outStr;
    }

    public static String encryptByPrivate(String str, String privateKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, priKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return outStr;
    }

    public static String decryptByPrivate(String str, String privateKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static String decryptByPublic(String str, String publicKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, pubKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
}
