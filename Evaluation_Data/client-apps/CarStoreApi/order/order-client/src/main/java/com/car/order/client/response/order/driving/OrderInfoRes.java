package com.car.order.client.response.order.driving;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="OrderInfoRes",description="订单信息VO")
public class OrderInfoRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "服务类型:0 即时 1 预约",name = "serviceType")
    private Integer serviceType;

    @ApiModelProperty(value = "订单金额",name = "orderAmount")
    private String orderAmount;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单备注信息",name = "orderRemark")
    private String orderRemark;

    @ApiModelProperty(value = "订单结束时间",name = "endTime")
    private String endTime;

    @ApiModelProperty(value = "出发地",name = "startPlace")
    private String startPlace;

    @ApiModelProperty(value = "目的地",name = "endPlace")
    private String endPlace;
}
