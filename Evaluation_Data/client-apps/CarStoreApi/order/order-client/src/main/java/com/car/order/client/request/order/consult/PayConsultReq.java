package com.car.order.client.request.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2021/1/2
 */
@Data
@ApiModel(value="PayConsultReq",description="支付咨询订单请求VO对象")
public class PayConsultReq {

    @NotBlank(message = "请输入订单uuid！")
    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @NotBlank(message = "请输入车主uuid！")
    @ApiModelProperty(value = "车主uuid",name = "carOwnerUuid")
    private String carOwnerUuid;

    @NotNull(message = "请输入支付方式！")
    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

}
