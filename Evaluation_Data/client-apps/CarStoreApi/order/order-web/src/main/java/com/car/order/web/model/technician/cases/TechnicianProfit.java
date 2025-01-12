package com.car.order.web.model.technician.cases;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/1/23
 */
@Table(name = "technician_profit")
@Data
public class TechnicianProfit extends BaseModelInfo {

    /**
     * 技师案例uuid
     */
    @Column(name = "case_uuid")
    private String caseUuid;

    /**
     * 收益类型1维修2案例3问答
     */
    @Column(name = "profit_type")
    private Integer profitType;

    /**
     * 金额
     */
    @Column(name = "amt")
    private BigDecimal amt;

    /**
     * 销量
     */
    @Column(name = "num")
    private Integer num;
}
