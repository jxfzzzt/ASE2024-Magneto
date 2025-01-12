package com.car.account.client.enums.technician;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2021/2/7
 */
@Getter
public enum TechnicianTypeEnum {

    /**
     * 技师类型对应字典表
     */
    ELECTRICAL_TECHNICIAN("1200", "机电技师"),
    SHEET_TECHNICIAN("1201", "钣金技师"),
    SPRAY_TECHNICIAN("1202", "喷涂技师"),
    DRIVING_TECHNICIAN("1203", "代驾技师"),
    BEAUTY_TECHNICIAN("1204", "洗车美容技师");

    private String value;
    private String desc;

    TechnicianTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (TechnicianTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
