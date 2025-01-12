package com.car.common.enums;

import lombok.Getter;

/**
 * 字典类型
 * @author: shb
 * @Description:
 * @Date: Create By 19:29
 */
@Getter
public enum DicTypeEnum {

    /**
     * 证件类型
     */
    DIC_CERTIFICATE_TYPE("1","证件类型"),
    DIC_DEVICE_TYPE("2","设备类型"),
    DIC_DEVICE_MODEL("3","设备型号"),
    ALARM_FREQUENCY("4","告警频次")
    ;

    private String type;
    private String name;

    DicTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据类型获取名称
     * @param value
     * @return
     */
    public static String enumOfName(Integer value) {
        for (DicTypeEnum enums : values()) {
            if (enums.getType().equals(value)) {
                return enums.getName();
            }
        }
        return null;
    }
}
