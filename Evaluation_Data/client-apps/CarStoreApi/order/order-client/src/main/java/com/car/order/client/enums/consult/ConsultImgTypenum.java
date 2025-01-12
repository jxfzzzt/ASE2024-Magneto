package com.car.order.client.enums.consult;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum ConsultImgTypenum {
    /**
     * 图片类型 0 咨询 1 回答
     */
    CONSULT_IMG(0, "咨询"),
    ANSWER_IMG(1, "回答"),
    ;
    private Integer value;
    private String desc;

    ConsultImgTypenum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ConsultImgTypenum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
