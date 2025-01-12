package com.car.account.web.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字段校验
 * @author cjw
 */
public class CheckFieldUtils {


    /**
     * 校验邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 检验手机号
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        boolean flag = false;
        if(phone.length() != 11){
            System.out.println("请输入11为的手机号");
            return false;
        }
        try {
//            String check = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
            String check = "^1[0-9][0-9]\\d{4,8}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(phone);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 校验出生年月是否未满18周岁
     * @param date
     * @return
     */
    public static boolean checkAdult(Date date) {
        Calendar current = Calendar.getInstance();
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(date);
        Integer year = current.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (year > 18) {
            return true;
        } else if (year < 18) {
            return false;
        }
        // 如果年相等，就比较月份
        Integer month = current.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
        if (month > 0) {
            return true;
        } else if (month < 0) {
            return false;
        }
        // 如果月也相等，就比较天
        Integer day = current.get(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH);
        return  day >= 0;
    }

    /**
     * 验证身份证号以及是否满十八岁
     * @param IdNO
     * @return
     */
    public static boolean IdNOToAge(String IdNO){
        int leh = IdNO.length();
        if(leh == 18 || leh == 15){
            String dates="";
            if (leh == 18) {
                dates = IdNO.substring(6, 14);
                Date date = DateUtil.strToDate(dates, DateUtil.YYYYMMdd.get());
                return checkAdult(date);
            }else{
                dates = IdNO.substring(6, 12);
                String dateDay = "19" + dates;
                Date date = DateUtil.strToDate(dateDay, DateUtil.YYYY_MM_dd.get());
                return checkAdult(date);
            }
        }
        return false;
    }
}
