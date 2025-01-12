package com.car.order.client.enums.driving;

import com.car.order.client.enums.goods.PayMethodEnum;
import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Getter
public enum ServiceTypeEnum {
    /**
     * 服务类型:0 即时 1 预约
     */
    IMMEDIATE(0, "即时"),
    BESPOKEN(1, "预约")
    ;
    private Integer value;
    private String desc;

    ServiceTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ServiceTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
