package com.car.account.web.model.technician;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 技师账户信息
 * @author xlj
 * @date 2020/12/19
 */
@Data
@Table(name = "technician_account")
public class TechnicianAccount extends BaseModelInfo {

    /**
     * 技师ID
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

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
     * 借记卡正面
     */
    @Column(name = "debit_card_url")
    private String debitCardUrl;

    /**
     * 借记卡反面
     */
    @Column(name = "debit_card_back_url")
    private String debitCardBackUrl;

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
     * 待提现金额
     */
    @Column(name = "wait_amount")
    private BigDecimal waitAmount;

    /**
     * 提现累计金额
     */
    @Column(name = "withdraw_amount")
    private BigDecimal withdrawAmount;

    @Column(name = "frozen_amt")
    private BigDecimal frozenAmt;



}
