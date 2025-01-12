package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 */
@Getter
public enum PaymentTypeEnum {
    JSAPI_GZH("JSAPI_GZH", "微信公众号"),
    JSAPI("JSAPI", "小程序/微信"),
    NATIVE("NATIVE", "PC扫码支付"),
    MWEB("MWEB", "H5浏览器"),
    ;
    private String value;
    private String desc;

    PaymentTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (PaymentTypeEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
