package com.car.order.client.enums.course;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/25
 */
@Getter
public enum PurchaseTypeEnum {

    /**
     * 课程类型 0:直播 1:图文
     */
    PURCHASE(0, "购买"),
    NOT_PURCHASE(1, "未购买")
    ;
    private Integer value;
    private String desc;

    PurchaseTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (PurchaseTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
