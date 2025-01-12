package com.car.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码加密
 */
public class BCryptPwdUtils {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String getBcryptPwd(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 匹配密码
     * @param rawPassword   铭文密码
     * @param encodedPassword   密文密码
     * @return
     */
    public static boolean matchesBcryptPwd(String rawPassword, String encodedPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(BCryptPwdUtils.matchesBcryptPwd("123456","$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq"));
    }
}
