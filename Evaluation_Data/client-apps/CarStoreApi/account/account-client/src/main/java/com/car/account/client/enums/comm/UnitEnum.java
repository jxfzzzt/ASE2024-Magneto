package com.car.account.client.enums.comm;

/**
 * @author zhangyp
 * @date 2021/1/9 19:53
 */
public enum UnitEnum {
    GE("个"),

    PI("瓶"),

    XI("箱"),

    BA("包")
    ;

    private String unit;

    public String getUnit() {
        return unit;
    }

    UnitEnum(String unit) {
        this.unit = unit;
    }
}
