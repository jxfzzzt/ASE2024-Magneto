package com.car.account.client.enums.withdrawal;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2021/1/3
 */
@Getter
public enum WithdrawalTypeEnum {

    /**
     * 提现类型 0:分类提现  1:全部提现
     */
    PARTIAL_WITHDRAWAL(0, "分类提现"),
    ALL_WITHDRAWAL(1, "全部提现");

    private Integer value;
    private String desc;

    WithdrawalTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (WithdrawalTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
