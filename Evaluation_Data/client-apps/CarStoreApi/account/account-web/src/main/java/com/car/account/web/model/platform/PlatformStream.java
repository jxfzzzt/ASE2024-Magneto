package com.car.account.web.model.platform;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 
 *
 * @author niushuaixiang
 * @createDate 2021-01-27
 */
@Data
@Table(name = "platform_stream")
@EqualsAndHashCode(callSuper = true)
public class PlatformStream extends BaseModelInfo {



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
     * 流水分类
     */
    @Column(name = "classify")
    private Integer classify;
    /**
     * 订单分类0 订单点评  1 咨询 2 回答 3 案例 4 旁听 5 维修保养 6 代驾 7 dtc故障 8 课程 10：共享技师
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;
}