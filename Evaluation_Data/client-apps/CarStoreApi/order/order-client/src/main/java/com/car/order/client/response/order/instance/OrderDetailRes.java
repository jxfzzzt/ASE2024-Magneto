package com.car.order.client.response.order.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value = "OrderDetailRes",description = "案例订单信息VO")
public class OrderDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "订单金额",name = "orderAmount")
    private String orderAmount;

    @ApiModelProperty(value = "应收金额",name = "receivableAmount")
    private String receivableAmount;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单备注信息",name = "orderRemark")
    private String orderRemark;

}
