package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum AfterSaleStsEnum {

    /**
     * 售后状态:0 等待买家退货 1 已退货 待收货 2 已收货 换货中 3 系统退款中 4 已完成 5 已取消',
     */
    WAITING_FOR_BUYER_RETURN(0, "等待买家退货"),
    RETURNED_GOODS_TO_BE_RECEIVED(1, "已退货 待收货"),
    RECEIVED_GOODS_IN_EXCHANGE(2, "已收货 换货中"),
    SYSTEM_REFUND_IN_PROGRESS(3, "系统退款中"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消")
    ;
    private Integer value;
    private String desc;

    AfterSaleStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (AfterSaleStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
