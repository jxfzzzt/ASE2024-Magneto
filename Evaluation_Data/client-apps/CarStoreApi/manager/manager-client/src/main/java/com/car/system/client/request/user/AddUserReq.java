package com.car.system.client.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xlj
 */
@Data
@ApiModel(value="AddUserReq",description="新增用户请求VO")
public class AddUserReq  {

    @NotBlank(message = "请输入用户名！")
    @ApiModelProperty(value = "登录名称",name = "username")
    private String username;

    @ApiModelProperty(value = "登录密码",name = "password")
    private String password;

    @NotBlank(message = "请输入用户密码！")
    @ApiModelProperty(value = "用户姓名",name = "name")
    private String name;

    @ApiModelProperty(value = "用户邮箱",name = "email")
    private String email;

    @ApiModelProperty(value = "用户手机",name = "phone")
    private String phone;

    @NotNull(message = "请选择用户状态！")
    @ApiModelProperty(value = "用户状态  0：开启  1：禁用",name = "status")
    private Integer status;

    @NotBlank(message = "请选择用户角色")
    @ApiModelProperty(value = "用户角色",name = "roleUuid")
    private String roleUuid;

}
