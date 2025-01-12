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
@ApiModel(value="UpdateOrderReq",description="修改商品订单")
public class UpdateServerOrderReq {

    @ApiModelProperty(value = "订单uuid")
    @NotBlank(message = "请输入订单uuid")
    private String orderUuid;

    @ApiModelProperty(value = "技师uuid")
    private String technicianUuid;

    @ApiModelProperty(value = "技师姓名")
    private String technicianName;

    @ApiModelProperty(value = "技师手机号")
    private String technicianMobile;

    @ApiModelProperty(value = "车辆进店里程数")
    private String carInMileage;

    @ApiModelProperty(value = "车辆出店里程数")
    private String carOutMileage;

    @ApiModelProperty(value = "订单备注信息")
    private String orderRemark;
}
