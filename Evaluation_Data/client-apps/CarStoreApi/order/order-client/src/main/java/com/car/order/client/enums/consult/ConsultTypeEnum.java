package com.car.order.client.enums.consult;

import lombok.Getter;

/**
 * @author xielinjiang
 * @date 2020/12/30
 */
@Getter
public enum ConsultTypeEnum {
    /**
     * 采纳结果 0 满意 1 不满意
     */
    technician_questions(1, "技师提问"),
    national_questions(2, "全国技师提问"),
    ;
    private Integer value;
    private String desc;

    ConsultTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ConsultTypeEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
