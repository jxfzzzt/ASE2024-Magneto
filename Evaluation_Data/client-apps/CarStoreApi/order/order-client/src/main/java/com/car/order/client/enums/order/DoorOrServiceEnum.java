package com.car.order.client.enums.order;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum DoorOrServiceEnum {
    /**
     * 现场下单服务类型 1技师到场，2 完成服务
     */

    SHOW_UP(1, "技师到场"),
    SERVICE_END(2, "完成服务")
    ;
    private Integer value;
    private String desc;

    DoorOrServiceEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (DoorOrServiceEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
