package com.car.common.enums;

/**
 * 数据状态枚举
 */
public enum OperEnum {

    /**
     * 新增
     */
    ADD("ADD", "新增"),
    UPDATE("UPDATE", "修改"),
    DELETE("DELETE", "删除"),
    SELECT("SELECT", "查询"),
    UPLOAD("UPLOAD", "上传"),
    BATCH("BATCH", "批量"),
    DOWNLOAD("DOWNLOAD", "下载"),
    DEFAULT("DEFAULT", "未知"),
    ;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static OperEnum format(String key) {
        for (OperEnum statusEnum : OperEnum.values()) {
            if (statusEnum.getCode().equals(key)) {
                return statusEnum;
            }
        }
        return null;
    }

    OperEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
