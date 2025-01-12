package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum PayMethodEnum {
    /**
     * 支付方式
     */
    WE_CHAT_PAY(0, "微信支付"),
    ALI_PAY(1, "支付宝支付")
    ;
    private Integer value;
    private String desc;

    PayMethodEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (PayMethodEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
