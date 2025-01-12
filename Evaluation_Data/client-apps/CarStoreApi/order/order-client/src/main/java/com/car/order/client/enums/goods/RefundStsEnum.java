package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum RefundStsEnum {
    /**
     * 退款状态:0 同意退款 1 拒绝退款 2 取消退款
     */
    AGREE_TO_REFUND(0, "同意退款"),
    REFUSE_REFUND(1, "拒绝退款"),
    CANCEL_REFUND(2, "取消退款")
    ;
    private Integer value;
    private String desc;

    RefundStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (RefundStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
