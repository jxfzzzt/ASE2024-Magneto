package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@Table(name = "store_account")
public class StoreAccount extends BaseModelInfo {

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 开户名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 开户银行,对应字典表code
     */
    @Column(name = "deposit_bank")
    private String depositBank;

    /**
     * 支行名称
     */
    @Column(name = "sub_branch_name")
    private String subBranchName;

    /**
     * 银行卡号
     */
    @Column(name = "card_numbers")
    private String cardNumbers;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 微信账号
     */
    @Column(name = "we_chat_account")
    private String weChatAccount;

    /**
     * 提现方式,对应字典表uuid
     */
    @Column(name = "withdrawal_way")
    private String withdrawalWay;

    /**
     * 账户类型,对应字典表uuid
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 账户现有余额
     */
    @Column(name = "account_amount")
    private BigDecimal accountAmount;

    /**
     * 历史累计金额
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 提现累计金额
     */
    @Column(name = "withdraw_amount")
    private BigDecimal withdrawAmount;

    /**
     * 待提现累计金额
     */
    @Column(name = "wait_amount")
    private BigDecimal waitAmount;

    /**
     * 冻结金额
     */
    @Column(name = "frozen_amt")
    private BigDecimal frozenAmt;
}
