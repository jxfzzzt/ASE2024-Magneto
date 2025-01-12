package com.car.order.client.enums.consult;

import lombok.Getter;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Getter
public enum OrderTypeEnum {
    /**
     * 订单类型 0 订单点评  1 咨询 2 回答 3 案例 4 旁听 5 维修保养 6 代驾 7 dtc故障 8 课程 10：共享技师
     */
    ORDER_REVIEW(0, "订单点评"),
    CONSULT(1, "咨询"),
    ANSWER(2, "回答"),
    EXAMPLE(3, "案例"),
    AUDITOR(4, "旁听"),
    GOOD(5, "维修保养"),
    DRIVING_AGENT(6, "代驾"),
    DTC(7, "dtc故障"),
    COURSE(8, "课程"),
    SCENE(9, "现场下单"),
    SHARED_TECHNICIAN(10, "共享技师"),
    SCENE_SERVICE(11, "现场服务"),
    ;
    private Integer value;
    private String desc;

    OrderTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String enumOfDesc(Integer value) {
        for (OrderTypeEnum enums : values()) {
            if (enums.value.equals(value)) {
                return enums.desc;
            }
        }
        return null;
    }
}
