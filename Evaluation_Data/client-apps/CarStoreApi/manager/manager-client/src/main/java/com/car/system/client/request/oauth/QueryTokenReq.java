package com.car.system.client.request.oauth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 接收页面请求获取token参数
 * @author linjiang.xie
 * @date 2019/6/3 11:02
 */
@Data
public class QueryTokenReq {

    @NotBlank(message = "请输入登录用户名！")
    @ApiModelProperty(value = "用户名",name = "username")
    private String username;

    @NotBlank(message = "请输入登录密码！")
    @ApiModelProperty(value = "密码",name = "password")
    private String password;

    @ApiModelProperty(value = "验证码",name = "verificationCode")
    @NotBlank(message = "请输入验证码！")
    private String verificationCode;

}
