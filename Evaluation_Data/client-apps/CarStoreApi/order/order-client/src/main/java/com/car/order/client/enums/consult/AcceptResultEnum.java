package com.car.order.client.enums.consult;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum AcceptResultEnum {
    /**
     * 采纳结果 0 满意 1 不满意
     */
    SATISFACTION(0, "满意"),
    UNSATISFACTORY(1, "不满意"),
    ;
    private Integer value;
    private String desc;

    AcceptResultEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (AcceptResultEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
