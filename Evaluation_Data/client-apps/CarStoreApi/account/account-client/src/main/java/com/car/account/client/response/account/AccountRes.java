package com.car.account.client.response.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 *
 * 账户信息
 * @author zhangyp
 * @date 2021/1/28 22:06
 */
@Data
@ApiModel
public class AccountRes {
    @ApiModelProperty("账户余额")
    private BigDecimal accountAmt;
    @ApiModelProperty("已提现金额")
    private BigDecimal withdrawAmt;
    @ApiModelProperty("可提现金额")
    private BigDecimal aviWithdrawAmt;
    @ApiModelProperty("待入账金额")
    private BigDecimal ingAmt;
    @ApiModelProperty("订单总数")
    private Integer orderNum;
}
