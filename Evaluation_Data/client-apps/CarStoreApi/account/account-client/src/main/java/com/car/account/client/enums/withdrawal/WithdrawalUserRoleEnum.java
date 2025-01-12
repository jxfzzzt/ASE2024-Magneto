package com.car.account.client.enums.withdrawal;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Getter
public enum  WithdrawalUserRoleEnum {

    /**
     * 提现用户角色 0 店铺 1 技师
     */
    STORE_WITHDRAWAL(0, "店铺提现"),
    TECHNICIAN_WITHDRAWAL(1, "技师提现");

    private Integer value;
    private String desc;

    WithdrawalUserRoleEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (WithdrawalUserRoleEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
