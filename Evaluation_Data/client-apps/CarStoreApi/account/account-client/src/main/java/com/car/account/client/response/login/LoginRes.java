package com.car.account.client.response.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/16 21:15
 */
@Data
@ApiModel
public class LoginRes {

    @ApiModelProperty(value = "授权token",name = "token")
    private String token;
    @ApiModelProperty(value = "有效期时间",name = "expires_in")
    private Integer expires_in;
    @ApiModelProperty(value = "用户类型 1:车主 2：技师 3：店铺",name = "userType")
    private Integer userType;
    @ApiModelProperty(value = "数据uuid",name = "uuid")
    private String uuid;
    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;
}
