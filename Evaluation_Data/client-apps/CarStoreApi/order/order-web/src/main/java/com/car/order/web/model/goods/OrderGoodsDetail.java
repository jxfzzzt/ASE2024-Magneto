package com.car.order.web.model.goods;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@Table(name = "order_goods_detail")
public class OrderGoodsDetail extends BaseModelInfo {

    /**
     * 订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 商品uuid
     */
    @Column(name = "goods_uuid")
    private String goodsUuid;
    /**
     * 商品uuid
     */
    @Column(name = "goods_detail_uuid")
    private String goodsDetailUuid;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品数量
     */
    @Column(name = "goods_num")
    private Integer goodsNum;

    /**
     * 商品图片地址
     */
    @Column(name = "goods_img_url")
    private String goodsImgUrl;

    /**
     * 工时费
     */
    @Column(name = "man_hour_cost")
    private BigDecimal manHourCost;

    /**
     * 材料费
     */
    @Column(name = "materials_expenses")
    private BigDecimal materialsExpenses;

    /**
     * 是否收取人工费用 0 收取 1 不收取
     */
    @Column(name = "is_serve_fee")
    private Integer isServeFee;

}
