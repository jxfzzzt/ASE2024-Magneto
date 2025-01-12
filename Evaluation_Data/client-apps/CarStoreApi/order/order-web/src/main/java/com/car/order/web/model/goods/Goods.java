package com.car.order.web.model.goods;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Data
@Table(name = "goods")
public class Goods extends BaseModelInfo {

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 商品类型
     */
    @Column(name = "goods_type")
    private String goodsType;

    /**
     * 父类型 一级分类
     */
    @Column(name = "parent_type")
    private String parentType;

    /**
     * 二级分类
     */
    @Column(name = "sub_type")
    private String subType;

    /**
     * 工时费 / 服务费
     */
    @Column(name = "man_hour_cost")
    private BigDecimal manHourCost;

    /**
     * 材料费 / 快递费
     */
    @Column(name = "materials_expenses")
    private BigDecimal materialsExpenses;

    /**
     * 销量
     */
    @Column(name = "sales_num")
    private Integer salesNum;

    /**
     * 库存
     */
    @Column(name = "surplus_num")
    private Integer surplusNum;

    /**
     * 销售状态:0 库存 下架 1 在售 上架
     */
    @Column(name = "sell_sts")
    private Integer sellSts;

    /**
     * 描述
     */
    @Column(name = "goods_describe")
    private String goodsDescribe;

    /**
     * 商品总金额
     */
    @Column(name = "amt")
    private BigDecimal amt;

    /**
     * 平台服务费用
     */
    @Column(name = "platform_service_money")
    private BigDecimal platformServiceMoney;

    /**
     * 车辆品牌
     */
    @Column(name = "vehicle_brand")
    private String vehicleBrand;

    /**
     * 车型类型
     */
    @Column(name = "vehicle_model")
    private String vehicleModel;
}
