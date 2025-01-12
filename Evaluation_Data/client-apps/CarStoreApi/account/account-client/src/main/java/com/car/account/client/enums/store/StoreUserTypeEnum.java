package com.car.account.client.enums.store;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/21
 */
@Getter
public enum StoreUserTypeEnum {

    /**
     * 店铺 人员类型
     *
     */
    ADMIN("501", "管理员"),
    SUPPORT("502", "技术支持"),
    FINANCE("503", "财务"),
    PART("504", "零件")
    ;
    private String value;
    private String desc;

    StoreUserTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(String value) {
        for (StoreUserTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
