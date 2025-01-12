package com.car.order.client.enums.consult;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum AnswerStsEnum {
    /**
     * 答复状态 0 未答复 1 已答复
     */
    UNANSWERED(0, "未答复"),
    ALREADY_ANSWERED(1, "已答复"),
    ;
    private Integer value;
    private String desc;

    AnswerStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (AnswerStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
