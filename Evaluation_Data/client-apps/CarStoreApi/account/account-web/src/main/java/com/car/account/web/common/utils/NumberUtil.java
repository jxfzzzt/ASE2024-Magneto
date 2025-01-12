package com.car.account.web.common.utils;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 数值计算工具类
 * @author cjw
 */

public class NumberUtil {

    /**
     * 生成短数字UUID
     * @return
     */
    public static String generateShortUuid() {
        String orderCode = DateUtil.dateToStr(new Date(),DateUtil.MMddHHmmss.get());
        Random random = new Random();
        String result="";
        for(int i=0;i<6;i++){
            //首字母不能为0
            orderCode += (random.nextInt(9)+1);
        }
        return orderCode;
    }

    public static String getSixNumber() {
        Random random = new Random();
        String result="";
        for(int i=0;i<6;i++){
            //首字母不能为0
            result += (random.nextInt(9)+1);
        }
        return result;

    }

    public static void main(String[] args) {
        System.out.println(NumberUtil.getSixNumber());
    }

    /**
     * 计算两个数的百分
     * 结果返回为整数，不保留小数值
     * @param num1 除数
     * @param num2 被除数
     * @return
     */
    public static String getPercent(int num1,int num2){
        //如果num2为0 则直接返回0，原因被除数不能为0
        if(num2 == 0){
            return "0";
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }
}
