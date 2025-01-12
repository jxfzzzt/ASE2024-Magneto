package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 */
@Getter
public enum ClearStatusEnum {

    UN_CLEAT(0, "未对账"),
    SUCCESS_CLEAR(1, "对账成功"),
    FALIURE_CLEAR(2, "对账失败");
    ;
    private Integer value;
    private String desc;

    ClearStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ClearStatusEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
