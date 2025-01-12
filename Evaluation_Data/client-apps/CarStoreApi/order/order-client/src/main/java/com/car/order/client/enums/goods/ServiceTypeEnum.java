package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-17 11:57
 */
@Getter
public enum ServiceTypeEnum {
    /**
     * 服务类型
     */
    IN_STORE_SERVICE(1, "到店服务"),
    EXPRESS(2, "快递");

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
