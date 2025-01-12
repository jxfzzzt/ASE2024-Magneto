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
@Table(name = "withdrawal_detail")
public class WithdrawalDetail extends BaseModelInfo {

    /**
     * 提现表ID
     */
    @Column(name = "withdrawal_uuid")
    private String withdrawalUuid;

    /**
     * 数据类型 1:提现  2:待入账
     */
    @Column(name = "withdrawal_type")
    private Integer withdrawalType;

    /**
     * 代驾金额
     */
    @Column(name = "driving_amount")
    private BigDecimal drivingAmount;

    /**
     * 维修金额
     */
    @Column(name = "service_amount")
    private BigDecimal serviceAmount;

    /**
     * 现场支持金额
     */
    @Column(name = "support_amount")
    private BigDecimal supportAmount;

    /**
     * 现场支持金额
     */
    @Column(name = "case_amount")
    private BigDecimal caseAmount;

    /**
     * 回答金额
     */
    @Column(name = "qa_amount")
    private BigDecimal qaAmount;
}
