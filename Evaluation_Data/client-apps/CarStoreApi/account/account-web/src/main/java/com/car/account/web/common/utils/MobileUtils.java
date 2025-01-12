package com.car.account.web.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linjiang.xie
 * @date 2020/3/31 12:35
 */
public class MobileUtils {
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(16[0-9])|(17[0-9])|(19[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static void main(String[] str){
        System.out.print(MobileUtils.isMobileNO("11621195447"));
    }
}
