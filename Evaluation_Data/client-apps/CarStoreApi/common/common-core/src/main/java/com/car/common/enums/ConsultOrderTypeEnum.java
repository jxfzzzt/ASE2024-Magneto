package com.car.common.enums;

/**
 * 咨询和旁听类型
 * @author zhangyp
 * @date 2021/1/31 19:44
 */
public enum ConsultOrderTypeEnum {

    ASK(1,"咨询"),

    Audit(4,"旁听")
    ;

    private Integer type;

    private String desc;

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    ConsultOrderTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
