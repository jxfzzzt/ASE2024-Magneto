package com.car.order.client.enums.order;

import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/16
 */
@Getter
public enum WhetherAgreeRefundEnum {
    /**
     * 是否同意退款 0：同意  1：不同意
     */
    AGREE(0,"同意"),
    NOT_AGREE(1,"不同意"),
    ;
    private Integer value;
    private String desc;

    WhetherAgreeRefundEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (WhetherAgreeRefundEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
