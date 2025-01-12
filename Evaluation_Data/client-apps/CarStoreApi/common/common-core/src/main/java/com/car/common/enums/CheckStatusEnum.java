package com.car.common.enums;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Getter
public enum CheckStatusEnum {

    /**
     * 店铺审核状态:0:待审核 1:审核通过 2:审核驳回
     */
    CHECK_PENDING(0, "待审核"),
    APPROVE(1, "审核通过"),
    CHECK_REJECTED(2, "审核驳回");
    private Integer value;
    private String desc;

    CheckStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (CheckStatusEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
