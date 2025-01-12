package com.car.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author linjiang.xie
 * @date 2019/12/12 15:50
 */
public class ExceptionUtils {
    /**
     * 转换异常信息为字符串
     */
    public static String stackTraceToString(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}
