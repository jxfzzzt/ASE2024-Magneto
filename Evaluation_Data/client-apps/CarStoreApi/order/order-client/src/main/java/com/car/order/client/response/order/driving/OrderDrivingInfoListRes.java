package com.car.order.client.response.order.driving;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="OrderDrivingInfoListRes",description="订单代驾列表信息VO")
public class OrderDrivingInfoListRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "服务类型:0 即时 1 预约",name = "serviceType")
    private Integer serviceType;

    @ApiModelProperty(value = "技师(代驾)姓名",name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "技师(代驾)手机号",name = "technicianMobile")
    private String technicianMobile;

    @ApiModelProperty(value = "订单开始时间",name = "createdTime")
    private String createdTime;

    @ApiModelProperty(value = "订单结束时间",name = "endTime")
    private String endTime;

    @ApiModelProperty(value = "订单金额",name = "orderAmount")
    private String orderAmount;

    @ApiModelProperty(value = "车主手机号",name = "carOwnerMobile")
    private String carOwnerMobile;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;
}
