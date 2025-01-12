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
public class ConfirmOrderReq {

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    @NotBlank(message = "请输入订单uuid")
    private String orderUuid;

    @ApiModelProperty(value = "订单类型 0 订单点评  1 咨询 2 回答 3 案例 4 旁听 5 维修保养 6 代驾 7 dtc故障 8 课程 10：共享技师", name = "orderType")
    private Integer orderType;
}
