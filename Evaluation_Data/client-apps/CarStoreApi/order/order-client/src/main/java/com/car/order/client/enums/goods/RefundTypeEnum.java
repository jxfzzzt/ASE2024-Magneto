package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum RefundTypeEnum {

    /**
     * 退款类型: 0 线上退款 1 线下退款
     */
    ONLINE_REFUND(0, "线上退款"),
    OFFLINE_REFUND(1, "线下退款")
            ;
    private Integer value;
    private String desc;

    RefundTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (RefundTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
