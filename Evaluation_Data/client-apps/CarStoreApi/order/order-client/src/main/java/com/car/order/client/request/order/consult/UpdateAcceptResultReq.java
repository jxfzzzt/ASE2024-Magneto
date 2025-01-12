package com.car.order.client.request.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2021/1/3
 */
@Data
@ApiModel(value="UpdateAcceptResultReq",description="更改咨询结果请求VO对象")
public class UpdateAcceptResultReq {

    @NotBlank(message = "请输入订单uuid！")
    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @NotNull(message = "请输入采纳结果！")
    @ApiModelProperty(value = "采纳结果 0 满意 1 不满意",name = "orderUuid")
    private Integer acceptResult;
}
