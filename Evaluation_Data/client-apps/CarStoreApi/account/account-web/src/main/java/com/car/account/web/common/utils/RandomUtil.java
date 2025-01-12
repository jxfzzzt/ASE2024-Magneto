package com.car.account.web.common.utils;

import java.util.Random;

/**
 * @author LX
 * @date 2020/3/17
 */
public class RandomUtil {
    /**
     * 生成指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandom(int length){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
    public static void main(String[] args) {
        System.out.println(getRandom(6));
    }
}

