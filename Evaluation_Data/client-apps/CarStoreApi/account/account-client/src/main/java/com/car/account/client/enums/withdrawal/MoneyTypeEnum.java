package com.car.account.client.enums.withdrawal;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2021/1/3
 */
@Getter
public enum MoneyTypeEnum {

    /**
     * 金额类型 0:已提现  1:待入账
     */
    HAVE_WITHDRAWAL(0, "已提现"),
    UNRECORDED(1, "待入账");

    private Integer value;
    private String desc;

    MoneyTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (MoneyTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
