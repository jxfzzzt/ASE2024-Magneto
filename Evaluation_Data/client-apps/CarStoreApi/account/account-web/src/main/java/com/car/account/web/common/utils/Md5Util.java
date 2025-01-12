package com.car.account.web.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author linjiang.xie
 * @date 2020/4/11 14:19
 */
@Slf4j
public class Md5Util {
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static void main(String[] asd) {
        String con = "hello kitty";
        String str = MD5Encode(con, "UTF-8");
        System.out.println(str.toUpperCase());
    }


    /**
     * 根据对象参数封装对接生成TreeMap对象
     * TreeMap自动使用字符串字母排序
     *
     * @return
     */
    public static Map getTreeMap(Object obj) {
        //使用treeMap，默认对属性名称字段进行字母排序
        Map<String, String> map = new TreeMap<>();
        //使用synchronizedMap为map增加线程安全处理
        Collections.synchronizedMap(map);

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            //获取当前属性名称
            String fieldName = field.getName();
            String value = null;
            try {
                value = String.valueOf(field.get(obj));
            } catch (IllegalAccessException e) {
                log.error("对象参数转换错误，入参：{}，错误原因：{}", obj, e.getMessage());
            }
            //当获取值为空时，跳过当前属性处理
            if (!StringUtils.isEmpty(value)) {
                map.put(fieldName, value);
            }
        }
        return map;
    }

    /**
     * 展商生成Sign签名方案
     *
     * @param map       数据对象map集合
     * @param secretKey 数据对象 secretKey
     * @return
     */
    public static String getExhibitSign(Map<String, String> map, String secretKey) {
        StringBuilder sign = new StringBuilder();
        sign.append(secretKey + ".");
        for (String key : map.keySet()) {
            sign.append(key);
            sign.append(".");
            sign.append(map.get(key));
            sign.append(".");
        }
        sign.append(secretKey);

        //返回生成Sign签名结果
        return MD5(sign.toString());
    }

}
