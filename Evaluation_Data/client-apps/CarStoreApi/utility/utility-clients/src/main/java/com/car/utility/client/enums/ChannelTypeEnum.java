package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 */
@Getter
public enum ChannelTypeEnum {

    weixin("weixin", "微信支付"),
    alipay("alipay", "支付宝"),
    ;
    private String value;
    private String desc;

    ChannelTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (ChannelTypeEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
