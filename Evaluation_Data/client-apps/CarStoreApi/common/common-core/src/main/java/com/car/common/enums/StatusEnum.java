package com.car.common.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 */
@Getter
public enum StatusEnum {

    /**
     *
     */
    NORMAL(0, "正常/在线"),
    PROHIBIT(1, "禁用"),
    OFFLINE(2, "离线"),
    WAIT_ACTIVE(3, "待激活"),
    HAS_ACTIVE(4, "已激活");

    private Integer value;
    private String desc;

    StatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (StatusEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static boolean contains(Integer value) {
        StatusEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            StatusEnum typeEnum = var1[var3];
            if (typeEnum.value.equals(value)) {
                return true;
            }
        }

        return false;
    }

}
