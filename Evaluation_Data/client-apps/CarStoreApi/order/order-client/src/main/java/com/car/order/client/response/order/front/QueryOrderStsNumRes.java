package com.car.order.client.response.order.front;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/26
 */


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="CarOwnerInfoRes",description="订单车主信息VO")
public class QueryOrderStsNumRes {

    @ApiModelProperty(value = "已完成数量", name = "completedCount")
    private Integer completedNum;

    @ApiModelProperty(value = "未付款数量", name = "unpaidNum")
    private Integer unpaidNum;

    @ApiModelProperty(value = "已付款未完成数量", name = "paidNotCompleteNum")
    private Integer paidNotCompleteNum;

    @ApiModelProperty(value = "已退款数量", name = "refunded")
    private Integer refunded;
}
