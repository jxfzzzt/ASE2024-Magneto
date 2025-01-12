package com.car.account.client.request.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author zhangyp
 * @date 2021/1/16 21:13
 */
@Data
@ApiModel
public class LoginReq {

    @ApiModelProperty(value = "登陆终端 vehicle：车主  merchant:（技师/店铺）",name = "terminal")
    private String terminal;

    @NotBlank(message = "请输入登陆账号")
    @ApiModelProperty(value = "登陆账号",name = "accountName")
    private String accountName;

    @NotBlank(message = "请输入验证码")
    @ApiModelProperty(value = "验证码",name = "code")
    private String code;
}
