package com.car.system.client.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xlj
 */
@Data
@ApiModel(value="UpdateUserPwdReq",description="修改用户密码请求VO")
public class UpdateUserPwdReq {

    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "原始密码",name = "originalPassword")
    private String originalPassword;

    @ApiModelProperty(value = "登录密码",name = "password")
    private String password;

}
