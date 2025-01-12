package com.car.account.web.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author linjiang.xie
 * @date 2020/3/27 17:38
 */
@Slf4j
public class RequestUtil {
    private static final String UNKNOWN = "unknown";

    /**
     * 获取当前请求线程的请求
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Optional.ofNullable(requestAttributes).map(ServletRequestAttributes::getRequest)
                .orElseThrow(()-> new NullPointerException("该次请求线程中，获取ServletRequestAttributes对象时，为null"));
        return Optional.of(request).orElseThrow(()-> new NullPointerException("该次请求线程中，获取HttpServletRequest对象时，为null"));
    }

    /**
     * 获取请求头参数
     * @param key
     * @return
     */
    public static String getRequestHeaderParam(String key) {
        return getRequest().getHeader(key);
    }

    /**
     * 获取请求body中参数
     * @param key
     * @return
     */
    public static String getRequestParam(String key) {
        return getRequest().getParameter(key);
    }

    /**
     * 获取请求主机IP地址，如果通过代理进来，则透过防火墙获取真实ip地址
     */
    public static final String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址，如果通过代理进来，则透过防火墙获取真实的IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (log.isDebugEnabled()) {
            log.info("getIpAdress(HttpServletRequest) - X -Forwarder-For -String ip={}", ip);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = proxyClientIp(request, ip);
            ip = wlProxyClientIp(request, ip);
            ip = httpClientIp(request, ip);
            ip = httpXForwardedFor(request, ip);
            ip = remoteAddr(request, ip);
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    private static String remoteAddr(HttpServletRequest request, String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest)-getRemoteAddr-String ip={}", ip);
            }
        }
        return ip;
    }

    private static String httpXForwardedFor(HttpServletRequest request, String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest)-HTTP_X_FORWARDED_FOR-String ip={}", ip);
            }
        }
        return ip;
    }

    private static String httpClientIp(HttpServletRequest request, String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest)-HTTP_CLIENT_IP-String ip={}", ip);
            }
        }
        return ip;
    }

    private static String wlProxyClientIp(HttpServletRequest request, String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest)-WL-Proxy-Client-IP-String ip={}", ip);
            }
        }
        return ip;
    }

    private static String proxyClientIp(HttpServletRequest request, String ip) {
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (log.isInfoEnabled()) {
                log.info("getIpAddress(HttpServletRequest)-Proxy-Client-IP-String ip={}", ip);
            }
        }
        return ip;
    }
}
