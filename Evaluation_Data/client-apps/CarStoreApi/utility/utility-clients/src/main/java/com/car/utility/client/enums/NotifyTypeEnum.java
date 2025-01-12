package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 */
@Getter
public enum NotifyTypeEnum {

    UN_NOTIFY(0, "未通知"),
    NOTIFY(1, "已通知")
    ;
    private Integer value;
    private String desc;

    NotifyTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (NotifyTypeEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
