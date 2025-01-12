package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum EvaluateStsEnum {

    /**
     * 评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评',
     */
    NO_COMMENT(0, "未评论"),
    COMMENTED(1, "已评论"),
    ;
    private Integer value;
    private String desc;

    EvaluateStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (EvaluateStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
