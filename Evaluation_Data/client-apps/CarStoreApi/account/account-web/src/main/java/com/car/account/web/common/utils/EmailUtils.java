package com.car.account.web.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linjiang.xie
 * @date 2020/3/31 12:40
 */
public class EmailUtils {
    public static boolean isEmail(String string) {
        if (string == null){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(string);
        if (m.matches()){
            return true;
        }else{
            return false;
        }
    }
}
