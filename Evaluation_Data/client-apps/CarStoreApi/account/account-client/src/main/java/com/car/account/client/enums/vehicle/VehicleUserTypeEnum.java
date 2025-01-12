package com.car.account.client.enums.vehicle;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum VehicleUserTypeEnum {

    /**
     * 角色类型枚举
     */
    REGISTER(1, "注册用户"),
    OWNER(2, "车主用户");
    private Integer value;
    private String desc;

    VehicleUserTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (VehicleUserTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
