package com.car.order.client.request.order.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/16
 */

@Data
@ApiModel(value = "ConfirmOrderReq", description = "确认订单接收参数VO")
public class OrderWhetherAgreeRefundReq {

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    @NotBlank(message = "请输入订单uuid")
    private String orderUuid;

    @ApiModelProperty(value = "是否同意退款0:同意，1:不同意", name = "whetherRefund")
    private Integer whetherRefund;
}
