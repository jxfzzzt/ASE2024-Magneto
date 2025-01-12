package com.car.system.client.response.oauth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xlj
 */
@Data
@ApiModel(value="QueryTokenRes",description="登录查询TOKEN返回VO对象")
public class QueryTokenRes {
    @ApiModelProperty(value = "授权token",name = "token")
    private String token;
    @ApiModelProperty(value = "有效期时间",name = "expires_in")
    private Integer expiresIn;
    @ApiModelProperty(value = "用户uuid",name = "uuid")
    private String uuid;
}
