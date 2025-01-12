package com.car.account.client.response.vehicle.vehicleUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class QueryOrderAmountRes {
    @ApiModelProperty(value = "完成订单数",name = "orderCount")
    private Integer orderCount;

    @ApiModelProperty(value = "支出总金额",name = "payTotalMoney")
    private String payTotalMoney;

    @ApiModelProperty(value = "剩余金额",name = "remainingMoney")
    private String remainingMoney;



}
