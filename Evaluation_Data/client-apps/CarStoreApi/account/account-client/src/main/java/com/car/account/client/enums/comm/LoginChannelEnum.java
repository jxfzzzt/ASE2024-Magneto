package com.car.account.client.enums.comm;

import lombok.Getter;

/**
 * @author zhouz.
 * @date 2020/12/22
 */
@Getter
public enum LoginChannelEnum {


    /**
     * 登陆渠道
     */
    VEHICLE("vehicle", "车主"),
    /**
     * 技师
     */
    TECHNICIAN("technician", "技师"),
    /**
     * 车主
     */
    STORE("store","店铺")
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
