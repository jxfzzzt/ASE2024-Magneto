package com.car.account.web.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linjiang.xie
 * @date 2020/5/23 17:28
 */
@Slf4j
public class RSAEncrypt {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    /*public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message,keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);
    }*/

    public static void main(String[] args) throws Exception {
        String messageEn = "jsUJ6odxDQeVGTVg9/AhvbnasgmADW+bufURoaSj/stTOHiOgDQM8I5tHnZPaDpciF3Ha3GdnwT4DXR47dB7MSt78q96AQ6sIPRZBYiui6HXVUXvfqut1ivBIlMaDcXDMeS0myLAhg6bMNPunWCkuHgT69vRGaQgdeQluxQA1WU=";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJV6we372JVG7L37rJx5PWwACn1rK6hqXNhnpt0bkYgnH1+to3g9843LmROkrJZX6td8Bd3Z7nd5fjff2fZU2w/OprNmG1fcHIBgDFRJVrJDglIoL5l0R83upgTUOXPUWnCHcQbDHM9FRcBsEvAZEm+CiN4MQ11vE85c/C/h6YKhAgMBAAECgYAFBufWtQ2Hl6cGjJw1du5V33p61UvWkh0lOfcmoHK4fXhUo2ZH+O1j62DuhC85ryM6s/UOjPG0GuXfwyt1WU00oWn7LjYWsm8GxnoTdrIQzZRZf36CvU5cCngkxNc09ZFQ+FpuNfMfDUhcyLWZxFigByUUu/H8hBZ/mVnyDaYdAQJBAPM45JH/bnfV6buweOmHZMIffHbiftLFzIy2mpjNmdTjyqxWRTOKH70BYVYjzp5KZN98mYZxOKIsJt/cpEHvRIkCQQCdVR1TQ8M7p40s2u9DSnuLAk2Ufo7N+5OjbHvF4LlioCsBRarAM8n312mlr6f9hgYEfLegc8Y+Ylj8tiXsJ3dZAkASV0H1OHvOEZV0j9QuWAVjFhi+pknF/yBzo/l4eFiXUy4fXMoPHuVRzV7Xrg+QpC+DItQ47STGJeowi2Tfi/lxAkAy312vGb8cH+5XLael44SVCzaYqF92nT0834waDLME//NH4wX2RgitWfrwNx8jq91wP/+EI0tbyqMpe6BBfvVpAkAXIxyMOKkH90yz7vsvK+v+E/sZc7f5TrW03d4ul3vy3SK1TNUILkFPZ3IYbn/CHLjxCfKvhGO8tB7H0F0UFlHg";
        String messageDe = decrypt(messageEn,privateKey);
        System.out.println("还原后的字符串为:" + messageDe);
        System.out.println(System.currentTimeMillis());
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) {
        try{
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte));
            return outStr;
        }catch (Exception ex){
            log.error("解密验证码参数错误，请求参数：{}",str);
            return null;
        }
    }
}
