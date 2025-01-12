package com.car.order.web.model.goods;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 
 *
 * @author zhangyp
 * @createDate 2021-01-09
 */
@Data
@Table(name = "goods_detail")
public class GoodsDetail extends BaseModelInfo {
    /**
     * 商品uuid
     */
    @Column(name = "goods_uuid")
    private String goodsUuid;

    /**
     * 商品明细名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 数量
     */
    @Column(name = "num")
    private Integer num;

    /**
     * 单位:个/瓶
     */
    @Column(name = "unit")
    private String unit;

    /**
     * 商品单价
     */
    @Column(name = "amt")
    private BigDecimal amt;

    /**
     * 商品实际支付价格
     */
    @Column(name = "act_amt")
    private BigDecimal actAmt;

    /**
     * 备用字段
     */
    @Column(name = "bak1")
    private String bak1;

    /**
     * 备用字段
     */
    @Column(name = "bak2")
    private String bak2;

    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;
}