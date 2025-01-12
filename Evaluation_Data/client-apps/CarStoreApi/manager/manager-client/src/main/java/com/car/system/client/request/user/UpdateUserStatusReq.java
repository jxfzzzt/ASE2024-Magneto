package com.car.system.client.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author xlj
 */
@ApiModel(value="UpdateUserStatusReq",description="修改用户状态VO对象")
@Data
public class UpdateUserStatusReq {

    @NotBlank(message = "缺少用户ID")
    @ApiModelProperty(value = "用户ID",name = "uuid")
    private String uuid;

    @NotBlank(message = "用户状态不能为空")
    @ApiModelProperty(value = "用户状态  0：开启  1：禁用",name = "status")
    private Integer status;

}
