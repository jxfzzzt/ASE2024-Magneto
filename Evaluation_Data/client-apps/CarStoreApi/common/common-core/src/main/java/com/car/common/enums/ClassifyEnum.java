package com.car.common.enums;

/**
 *
 * 流水分类
 * @author zhangyp
 * @date 2021/1/27 21:36
 */
public enum ClassifyEnum {

    DJ(1,"代驾"),

    WX(2,"维修"),

    AL(3,"案例"),

    HD(4,"回答"),

    SP(5,"商品售卖")

    ;

    private Integer type;

    private String desc;

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    ClassifyEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
