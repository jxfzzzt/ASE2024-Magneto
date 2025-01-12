package com.car.account.client.response.withdrawal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@ApiModel(value="WithdrawalRes",description="提现VO")
public class WithdrawalRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户 uuid (店铺、技师等)",name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value = "用户名称或店铺名称",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号码",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺类型名称",name = "storeTypeName")
    private String storeTypeName;

    @ApiModelProperty(value = "提现用户角色 0 店铺 1 技师",name = "userRole")
    private Integer userRole;

    @ApiModelProperty(value = "提现金额",name = "withdrawalAmount")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "剩余金额",name = "balanceAmount")
    private BigDecimal balanceAmount;

    @ApiModelProperty(value = "当前账户总金额",name = "totalAmount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "当前账户待入账金额",name = "waitAmount")
    private BigDecimal waitAmount;

    @ApiModelProperty(value = "提现详情",name = "withdrawalDetailResList")
    private List<WithdrawalDetailRes> withdrawalDetailResList;

    @ApiModelProperty(value = "开户银行名称",name = "depositBank")
    private String depositBank;

    @ApiModelProperty(value = "支行名称",name = "subBranchName")
    private String subBranchName;

    @ApiModelProperty(value = "银行卡号",name = "cardNumbers")
    private String cardNumbers;

    @ApiModelProperty(value = "转账凭证图片url",name = "voucherImgUrl")
    private String voucherImgUrl;

    @ApiModelProperty(value = "审核状态 (0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "驳回详情",name = "rejectDetail")
    private String rejectDetail;

}
