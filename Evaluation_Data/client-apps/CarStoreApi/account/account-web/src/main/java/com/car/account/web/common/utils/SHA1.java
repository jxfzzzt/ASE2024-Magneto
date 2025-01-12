/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.car.account.web.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
public class SHA1 {


    public static String getHwImSHA1(String sign_str){
        MessageDigest md = null;
        StringBuffer hexstr = new StringBuffer();

        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(sign_str.getBytes());
            byte[] digest = md.digest();

            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexstr.toString();
    }

    public static String getHwImSHA256(String sign_str){
        MessageDigest md = null;
        StringBuffer hexstr = new StringBuffer();

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(sign_str.getBytes());
            byte[] digest = md.digest();

            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexstr.toString();
    }
}
