package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum FileTypeEnum {
    PERSON("person", "人脸图片"),
    OTHER("other", "其他文件"),
    ;
    private String value;
    private String desc;

    FileTypeEnum(String value,  String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (FileTypeEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

    public static String enumOfSmsCode(String value) {
        for (FileTypeEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
