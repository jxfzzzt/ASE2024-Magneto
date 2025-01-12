package com.car.common.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author suhaibo
 */
@Getter
public enum StsEnum {
    /**
     * 数据状态枚举
     */
    ACTIVE(0, "有效"),
    INVALID(1, "无效");
    private Integer value;
    private String desc;

    StsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (StsEnum enums :
                values()) {
            if (enums.value.equals(value) ) {
                return enums.desc;
            }
        }
        return null;
    }


}
