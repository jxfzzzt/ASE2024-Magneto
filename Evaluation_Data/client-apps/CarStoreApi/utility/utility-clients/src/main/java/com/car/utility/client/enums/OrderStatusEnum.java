package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 */
@Getter
public enum OrderStatusEnum {

    WAIT_PAYMENT(1, "待支付"),
    SUCCESS_PAYMENT(2, "支付成功"),
    FAILURE_PAYMENT(3,"支付失败"),
    WAIT_REFUND(4,"退款中"),
    SUCCESS_REFUND(5,"支付失败"),
    FAILURE_REFUND(6,"支付失败"),
    ;
    private Integer value;
    private String desc;

    OrderStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (OrderStatusEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
