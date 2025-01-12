package com.car.account.web.model.profit;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 
 *
 * @author zhangyp
 * @createDate 2021-01-27
 */
@Data
@Table(name = "profit_stream")
@EqualsAndHashCode(callSuper = true)
public class ProfitStream extends BaseModelInfo {
    /**
     * 人员uuid
     */
    @Column(name = "user_uuid")
    private String userUuid;

    @Column(name = "user_type")
    private Integer userType;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 金额
     */
    @Column(name = "amt")
    private BigDecimal amt;

    /**
     * 流水类型0收入1支出
     */
    @Column(name = "stream_type")
    private Integer streamType;

    /**
     * 流水分类1代驾2维修3案例4回答5商品售卖
     */
    @Column(name = "classify")
    private Integer classify;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;
}