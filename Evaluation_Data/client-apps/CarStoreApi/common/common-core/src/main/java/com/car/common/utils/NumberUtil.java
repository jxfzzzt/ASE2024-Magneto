package com.car.common.utils;

import java.text.NumberFormat;

/**
 * 数值计算工具类
 * @author fengwenyuan
 */

public class NumberUtil {

    /**
     * 计算两个数的百分
     * 结果返回为整数，不保留小数值
     * @param num1 除数
     * @param num2 被除数
     * @return
     */
    public static int getPercent(int num1,int num2){
        //如果num2为0 则直接返回0，原因被除数不能为0
        if(num2 == 0 || num1 ==0){
            return 0;
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        int result = Integer.valueOf(numberFormat.format((float) num1 / (float) num2 * 100));
        return result;
    }
}
