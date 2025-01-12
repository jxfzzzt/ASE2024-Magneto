package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum OrderStsEnum {
    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败',
     */
    UNPAID(0, "待支付"),
    HAVE_PAID(1, "已支付"),
    CANCELED (2, "已取消"),
    A_REFUND_OF(3, "退款中"),
    REFUND_SUCCESS(4, "退款成功"),
    REFUND_FAILURE(5, "退款失败"),
    COMPLETED(6, "已完成"),
    ;
    private Integer value;
    private String desc;

    OrderStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (OrderStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
