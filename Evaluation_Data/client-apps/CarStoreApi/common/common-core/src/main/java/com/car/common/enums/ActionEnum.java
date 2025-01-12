package com.car.common.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 */
@Getter
public enum ActionEnum {

    /**
     *
     */
    INSERT("INSERT", "新增"),
    UPDATE("UPDATE", "修改"),
    DELETE("DELETE", "删除"),
    MOVE("MOVE","移动"),
    ;

    private String value;
    private String desc;

    ActionEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (ActionEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static boolean contains(String value) {
        for (ActionEnum typeEnum : ActionEnum.values()) {
            if (typeEnum.value.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
