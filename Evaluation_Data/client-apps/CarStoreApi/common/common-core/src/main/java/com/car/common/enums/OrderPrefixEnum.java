package com.car.common.enums;

/**
 * @author zhangyp
 * @date 2021/1/24 17:32
 */
public enum OrderPrefixEnum {

    /**
     * 维修
     */
    WX("WX"),

    /**
     * 代驾
     */
    DJ("DJ"),

    /**
     * 咨询
     */
    ZX("ZX"),

    /**
     * 旁听
     */
    PT("PT"),

    /**
     * 案例
     */
    AL("AL"),

    /**
     * DTC故障
     */
    DTC("DTC"),

    /**
     * 课程
     */
    KC("KC"),

    /**
     * 现场订单
     */
    XC("XC"),

    /**
     * 共享技师
     */
    GX("GX"),
    ;


    private String type;

    OrderPrefixEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
