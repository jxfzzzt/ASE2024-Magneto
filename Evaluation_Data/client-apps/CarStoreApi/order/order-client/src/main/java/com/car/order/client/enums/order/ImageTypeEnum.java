package com.car.order.client.enums.order;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum ImageTypeEnum {
    /**
     * 现场下单服务类型 1：dtc,  2:故障描述 3技师到场，4 完成服务
     */
    DTC(1, "dtc"),
    FAULT_DESC(2, "故障描述"),
    SHOW_UP(3, "技师到场"),
    SERVICE_END(4, "完成服务")
    ;
    private Integer value;
    private String desc;

    ImageTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ImageTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
