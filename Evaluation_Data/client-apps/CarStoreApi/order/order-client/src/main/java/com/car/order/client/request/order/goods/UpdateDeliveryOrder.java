package com.car.order.client.request.order.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/15
 */
@Data
@ApiModel(value = "UpdateDeliveryOrder", description = "修改订单物流信息请求VO")
public class UpdateDeliveryOrder {

    @ApiModelProperty(value = "订单uuid")
    @NotBlank(message = "请输入订单uuid")
    private String orderUuid;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "配送地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "订单备注信息")
    private String orderRemark;
}
