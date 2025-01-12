package com.car.utility.client.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 * @author xlj
 */
@Getter
public enum FileChannelEnum {

    oss("oss", "阿里云文件存储"),
    obs("obs", "华为云文件存储"),
    local("local", "本地文件存储"),
    ;
    private String value;
    private String desc;

    FileChannelEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (FileChannelEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }

}
