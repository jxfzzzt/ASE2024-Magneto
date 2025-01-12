package com.car.common.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author zhangyp
 * @date 2021/1/16 21:40
 */
@Getter
public enum LoginChannelEnum {

    MANAGE("manage", "pc管理平台"),
    WECHAT("weChat", "微信小程序"),
    WEB_PC("webPC", "官网portal"),
    H5("h5", "H5")
    ;
    private String value;
    private String desc;

    LoginChannelEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (LoginChannelEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
