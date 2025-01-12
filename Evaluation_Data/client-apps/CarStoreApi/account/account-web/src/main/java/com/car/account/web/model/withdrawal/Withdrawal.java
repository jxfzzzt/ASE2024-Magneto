package com.car.account.web.model.withdrawal;

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
@Table(name = "withdrawal")
public class Withdrawal extends BaseModelInfo {

    /**
     * 用户 uuid (店铺、技师等)
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 用户名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 店铺名称
     */
    @Column(name = "store_name")
    private String storeName;

    /**
     * 店铺类型名称
     */
    @Column(name = "store_type_name")
    private String storeTypeName;

    /**
     * 提现用户角色 0 店铺 1 技师
     */
    @Column(name = "user_role")
    private Integer userRole;

    /**
     * 提现金额
     */
    @Column(name = "withdrawal_amount")
    private BigDecimal withdrawalAmount;

    /**
     * 剩余金额
     */
    @Column(name = "balance_amount")
    private BigDecimal balanceAmount;

    /**
     * 当前账户总金额
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 待入账金额
     */
    @Column(name = "wait_amount")
    private BigDecimal waitAmount;

    /**
     * 开户银行名称
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
     * 转账凭证图片url
     */
    @Column(name = "voucher_img_url")
    private String voucherImgUrl;

    /**
     * 审核状态 (0:待审核 1:审核通过 2:审核驳回)
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 驳回详情
     */
    @Column(name = "reject_detail")
    private String rejectDetail;

}
