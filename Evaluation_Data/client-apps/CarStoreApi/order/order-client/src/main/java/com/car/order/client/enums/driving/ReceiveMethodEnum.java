package com.car.order.client.enums.driving;

/**
 * 配送方式
 * @author zhangyp
 * @date 2021/1/15 22:30
 */
public enum ReceiveMethodEnum {

    /**
     * 快递
     */
    EXPRESS(0),

    /**
     * 自取
     */
    SELF_GET(1)
    ;

    private Integer code;

    ReceiveMethodEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
