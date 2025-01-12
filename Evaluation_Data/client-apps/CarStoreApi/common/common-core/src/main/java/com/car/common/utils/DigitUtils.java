package com.car.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算工具类
 * @author zhangyp
 * @date 2021/1/15 20:42
 */
public class DigitUtils {

    private static final int SCALE = 2;

    /**
     * 加法
     * @param values
     * @return
     */
    public static BigDecimal add(BigDecimal... values){

        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal val : values){

            if(null != val){

                sum = sum.add(val);
            }
        }
        return sum.setScale(SCALE,RoundingMode.HALF_UP);
    }

    /**
     * 减法
     * @param source
     * @param values
     * @return
     */
    public static BigDecimal subtract(BigDecimal source,BigDecimal... values){

        for(BigDecimal val : values){

            source = source.subtract(val);
        }
        return source.setScale(SCALE,RoundingMode.HALF_UP);
    }

    /**
     * 除法
     * @param source
     * @param values
     * @return
     */
    public static BigDecimal divide(BigDecimal source,BigDecimal... values){

        for(BigDecimal val : values){

            source = source.divide(val,SCALE,RoundingMode.HALF_UP);
        }
        return source;
    }

    /**
     * 乘法
     * @param values
     * @return
     */
    public static BigDecimal multiply(BigDecimal... values){

        BigDecimal m = BigDecimal.ONE;
        for(BigDecimal val : values){

            m = m.multiply(val);
        }
        return m.setScale(SCALE,RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {

        BigDecimal a = BigDecimal.valueOf(100.08);
        BigDecimal b = BigDecimal.valueOf(11.05);
        BigDecimal c = BigDecimal.valueOf(19.03);
        BigDecimal d = BigDecimal.valueOf(20.01);

        System.out.println(add(a,b,c,d));
        System.out.println(subtract(a,b,c,d));

        System.out.println(divide(a,b,c,d));
        System.out.println(multiply(a,b,c,d));
    }
}
