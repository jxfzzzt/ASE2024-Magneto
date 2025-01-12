package com.car.order.client.enums.platform;

import lombok.Getter;

/**
 * @program: car-service
 * @description: 平台收费科目
 * @author: niushuaixiang
 * @create: 2021-04-16 10:33
 */
@Getter
public enum PlatformClassifyEnum {



    SERVICE(1,"平台服务费"),
    ACCOUNT(2,"账户金额"),
    FEE(3,"平台收入"),
    SUBSIDY(4,"平台补贴")
    ;

    private Integer value;
    private String desc;

    PlatformClassifyEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (PlatformClassifyEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}