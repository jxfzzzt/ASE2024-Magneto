package com.car.system.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum RoleTypeEnum {

    /**
     * 角色类型枚举
     */
    MANAGE(0, "管理员"),
    ORDINARY(1, "普通用户");
    private Integer value;
    private String desc;

    RoleTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (RoleTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
