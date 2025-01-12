package com.car.order.client.enums.front;

import com.car.order.client.enums.goods.AfterSaleStsEnum;
import lombok.Getter;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/24
 */
@Getter
public enum QueryStateEnum {
    /**
     * APP查询状态
     * 状态 0 全部 1 待付款 2: 待服务 3:待评价  4:退款/售后
     */

    WHOLE(0, "全部"),
    UNPAID(1, "待付款"),
    STAY_SERVICE(2, "待服务"),
    STAY_EVALUATE(3, "待评价"),
    REFUND(4, "退款"),
    ;

    private Integer value;
    private String desc;

    QueryStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (QueryStateEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
