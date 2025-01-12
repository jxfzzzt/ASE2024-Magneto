package com.car.order.web.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量对象
 * @author 谢林江
 *
 */
@Component
@Data
@ConfigurationProperties(prefix="config")
public class ConfigConsts {

    /**
     * 旁听订单支付金额
     */
    private Integer auditorOrderMoney;

    /**
     * dtc订单支付金额
     */
    private String dtcOrderMoney;

    /**
     * dtc订单查看最多次数
     */
    private Integer dtcOrderNum;

    /**
     * 最新的N条教程
     */
    private Integer newCourseParentNum;

}
