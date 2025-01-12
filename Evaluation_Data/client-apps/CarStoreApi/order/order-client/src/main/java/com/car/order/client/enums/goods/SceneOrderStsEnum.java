package com.car.order.client.enums.goods;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum SceneOrderStsEnum {
    /**
     * 订单状态 0：待抢单  1:待支付 2：待上门,3:已上门,4:提交方案,5:待付款,6:服务中,7:服务完成,8:待评分,9:退款中,10:退款成功,11:退款失败,12:已完成
     */
    HAVE_PAID(0, "待抢单"),
    UNPAID(1, "待支付"),
    WAIT_DOOR(2, "待上门"),
    SUBMIT_PLAN(3, "提交方案"),
    WAIT_PAYMENT (4, "待付款"),
    IN_SERVICE (5, "服务中"),
    SERVICE_END (6, "服务完成"),
    WAIT_SCORE(7, "待评分"),
    A_REFUND_OF(8, "退款中"),
    REFUND_SUCCESS(9, "退款成功"),
    REFUND_FAILURE(10, "退款失败"),
    COMPLETED(11, "已完成"),

    ;
    private Integer value;
    private String desc;

    SceneOrderStsEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (SceneOrderStsEnum enums :values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
