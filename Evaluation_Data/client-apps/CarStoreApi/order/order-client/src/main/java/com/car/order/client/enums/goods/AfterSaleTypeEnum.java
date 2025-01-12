package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum AfterSaleTypeEnum {

    /**
     * 售后类型  0 退款 1 退货退款 2 退换货
     */
    REFUND(0, "退款"),
    RETURN_REFUND(1, "退货退款"),
    RETURN_POLICY(2, "退换货")
    ;
    private Integer value;
    private String desc;

    AfterSaleTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (AfterSaleTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
