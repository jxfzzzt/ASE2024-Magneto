package com.car.common.enums;

/**
 * @author zhangyp
 * @date 2021/1/16 21:16
 */
public enum UserTypeEnum {


    vehicle(1,"车主"),

    technician(2,"技师"),

    store(3,"店铺")
    ;

    private Integer type;

    private String desc;

    UserTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
