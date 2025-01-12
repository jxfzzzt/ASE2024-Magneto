package com.car.system.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum LoginChannelEnum {

    /**
     * 数据状态枚举
     */
    MANAGE("manage", "pc管理平台"),
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
