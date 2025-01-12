package com.car.order.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
public class PayOrderInfoDto {

    @ApiModelProperty(value = "订单时间", name = "orderTime")
    private Date orderTime;

    @ApiModelProperty(value = "订单金额", name = "payAmount")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "商品名称", name = "goodsName")
    private String goodsName;
}
