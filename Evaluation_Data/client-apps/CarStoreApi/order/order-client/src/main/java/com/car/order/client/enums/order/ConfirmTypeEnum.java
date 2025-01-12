package com.car.order.client.enums.order;

import com.car.order.client.enums.goods.OrderStsEnum;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/16
 */
@Getter
public enum ConfirmTypeEnum {

    /**
     * 确认人类型0：车主，1：店铺/技师
     */
    vehicle(0,"车主"),
    technician_store(1,"店铺/技师"),
    ;
    private Integer value;
    private String desc;

    ConfirmTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ConfirmTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
