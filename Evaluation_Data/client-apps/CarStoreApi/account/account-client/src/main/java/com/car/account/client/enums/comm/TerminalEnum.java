package com.car.account.client.enums.comm;

import lombok.Getter;

/**
 * @author 登陆终端.
 * @date 2020/12/22
 */
@Getter
public enum TerminalEnum {


    /**
     * 登陆渠道
     */
    VEHICLE("vehicle", "车主"),
    /**
     * 技师
     */
    MERCHANT("merchant", "商户（含技师/店铺）"),
    ;
    private String value;
    private String desc;

    TerminalEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (TerminalEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
