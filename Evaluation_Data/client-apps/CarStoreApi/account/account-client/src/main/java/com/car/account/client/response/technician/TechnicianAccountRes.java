package com.car.account.client.response.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianAccountRes {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "技师ID",name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "开户名",name = "accountName")
    private String accountName;

    @ApiModelProperty(value = "开户银行",name = "depositBank")
    private String depositBank;

    @ApiModelProperty(value = "支行名称",name = "subBranchName")
    private String subBranchName;

    @ApiModelProperty(value = "银行卡号",name = "cardNumbers")
    private String cardNumbers;

    @ApiModelProperty(value = "支付宝账号",name = "alipayAccount")
    private String alipayAccount;

    @ApiModelProperty(value = "微信账号",name = "weChatAccount")
    private String weChatAccount;

    @ApiModelProperty(value = "借记卡正面",name = "debitCardUrl")
    private String debitCardUrl;

    @ApiModelProperty(value = "借记卡反面",name = "debitCardBackUrl")
    private String debitCardBackUrl;

    @ApiModelProperty(value = "账户现有余额",name = "accountAmount")
    private BigDecimal accountAmount;

    @ApiModelProperty(value = "历史累计金额",name = "totalAmount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "待提现金额",name = "waitAmount")
    private BigDecimal waitAmount;

    @ApiModelProperty(value = "提现累计金额",name = "withdrawAmount")
    private BigDecimal withdrawAmount;




}
