package com.car.order.client.enums.sharetechnicianorder;

import lombok.Getter;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.client.enums.sharetechnicianorder
 * @NAME: ShareTechnicianOrderEnum
 * @DATE: 2021/3/4 23:39
 */
@Getter
public enum ShareTechnicianOrderEnum {

    WaitingForOrder(1, "待付款"),
    PendingOrder(6, "待接单"),
    SuccessfullyReceivedTheOrder(2, "待服务"),
    CancelTheOrder(3, "已完成"),
    OrderCompleted(4, "退款中"),
    Refunded(5, "已退款");

    private Integer value;

    /**
     * 订单预约状态
     * 1：待付款
     * 2：待服务
     * 3：已完成
     * 4：退款中
     * 5：已退款
     */
    private String desc;

    ShareTechnicianOrderEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (ShareTechnicianOrderEnum enums :
                values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return "订单错误";
    }
}
