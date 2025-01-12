package com.car.order.client.enums.front;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Getter
public enum GoodsOrderTypeEnum {

    /**
     * 维修保养订单类型 0 线上直接发货，无需服务， 1 购买商品且需要服务， 2 无商品，仅线下服务
     */
    NOT_SERVICE(0, "线上直接发货，无需服务"),
    SERVICE(1, "购买商品且需要服务"),
    NOT_GOODS(2, "无商品，仅线下服务"),
    ;

    private Integer value;
    private String desc;

    GoodsOrderTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (GoodsOrderTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
