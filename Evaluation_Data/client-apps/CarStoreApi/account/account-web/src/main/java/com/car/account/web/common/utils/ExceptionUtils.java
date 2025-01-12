package com.car.account.web.common.utils;;

/**
 * @author linjiang.xie
 * @date 2019/12/12 15:50
 */
public class ExceptionUtils {
    /**
     * 转换异常信息为字符串
     */
    public static String stackTraceToString(Exception e) {
        //异常名称
        String exceptionName = e.getClass().getName();
        //异常信息
        String exceptionMessage = e.getMessage();
        //堆栈信息
        StackTraceElement[] elements = e.getStackTrace();
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
