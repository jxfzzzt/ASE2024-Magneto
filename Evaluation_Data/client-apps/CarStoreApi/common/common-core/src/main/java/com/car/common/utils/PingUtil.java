package com.car.common.utils;

import java.net.InetAddress;

/**
 * Ping工具类
 */
public class PingUtil {
    public static boolean ping(String ipAddress) {
        int  timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = false;     // 当返回值是true时，说明host是可用的，false则不可。
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
