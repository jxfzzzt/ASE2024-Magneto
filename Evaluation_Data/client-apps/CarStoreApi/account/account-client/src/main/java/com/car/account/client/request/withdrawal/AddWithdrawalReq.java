package com.car.account.client.request.withdrawal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@ApiModel
public class AddWithdrawalReq {


    @NotBlank(message = "请输入用户 uuid！")
    @ApiModelProperty(value = "用户 uuid (店铺、技师等)",name = "userUuid")
    private String userUuid;

    @NotNull(message = "请输入用户角色！")
    @ApiModelProperty(value = "提现用户角色 0 店铺 1 技师",name = "userRole")
    private Integer userRole;

    @NotNull(message = "请输入提现类型！")
    @ApiModelProperty(value = "提现类型 0:分类提现  1:全部提现",name = "withdrawalType")
    private Integer withdrawalType;

    @ApiModelProperty(value = "提现详情",name = "withdrawalDetailReq")
    private WithdrawalDetailReq withdrawalDetailReq;

}
