package com.car.order.client.request.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhouz
 * @date 2021/1/2
 */
@Data
@ApiModel(value="AddAuditorReq",description="新增旁听订单请求VO对象")
public class AddAuditorReq {

    @NotBlank(message = "请输入订单uuid！")
    @ApiModelProperty(value = "需要旁听的订单uuid",name = "carOwnerUuid")
    private String orderUuid;
}
