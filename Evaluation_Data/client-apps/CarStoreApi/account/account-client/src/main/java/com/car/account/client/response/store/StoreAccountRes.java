package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@ApiModel(value="StoreAccountRes",description="店铺账号VO")
public class StoreAccountRes {

    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @NotEmpty(message = "请选择提现方式！")
    @ApiModelProperty(value = "提现方式,对应字典表 uuid",name = "withdrawalWay")
    private String withdrawalWay;

    @NotBlank(message = "请选择账户类型！")
    @ApiModelProperty(value = "账户类型,对应字典表 uuid",name = "accountType")
    private String accountType;

    @NotBlank(message = "请输入开户名！")
    @ApiModelProperty(value = "开户名",name = "accountName")
    private String accountName;

    @NotBlank(message = "请输入开户银行！")
    @ApiModelProperty(value = "开户银行",name = "depositBank")
    private String depositBank;

    @NotBlank(message = "请输入支行名称！")
    @ApiModelProperty(value = "支行名称",name = "subBranchName")
    private String subBranchName;

    @NotBlank(message = "请输入银行卡号！")
    @ApiModelProperty(value = "银行卡号",name = "cardNumbers")
    private String cardNumbers;

    @ApiModelProperty(value = "支付宝账号",name = "alipayAccount")
    private String alipayAccount;

    @ApiModelProperty(value = "微信账号",name = "weChatAccount")
    private String weChatAccount;
}
