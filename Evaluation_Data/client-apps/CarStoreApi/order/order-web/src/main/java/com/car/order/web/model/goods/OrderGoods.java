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
@Table(name = "order_goods")
public class OrderGoods extends BaseModelInfo {

    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 订单号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 服务地区,保存具体地址
     */
    @Column(name = "service_area")
    private String serviceArea;

    /**
     * 服务单号
     */
    @Column(name = "service_num")
    private String serviceNum;

    /**
     * 实收金额 包含快递费 或服务费
     */
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;

    /**
     * 应收金额
     */
    @Column(name = "receivable_amount")
    private BigDecimal receivableAmount;

    /**
     * 快递费
     */
    @Column(name = "amt_express")
    private BigDecimal amtExpress;
    /**
     * 服务费
     */
    @Column(name = "amt_service")
    private BigDecimal amtService;



    /**
     * 联系人
     */
    @Column(name = "contacts")
    private String contacts;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 支付方式
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    @Column(name = "order_sts")
    private Integer orderSts;

    /**
     * 配送方式
     */
    @Column(name = "delivery_mode")
    private Integer deliveryMode;

    /**
     * 配送地址
     */
    @Column(name = "delivery_address")
    private String deliveryAddress;

    /**
     * 订单备注信息
     */
    @Column(name = "order_remark")
    private String orderRemark;

    /**
     * 退款类型: 0 线上退款 1 线下退款
     */
    @Column(name = "refund_type")
    private Integer refundType;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 售后原因
     */
    @Column(name = "after_sale_cause")
    private String afterSaleCause;

    /**
     * 售后类型  0 退款 1 退货退款 2 退换货
     */
    @Column(name = "after_sale_type")
    private Integer afterSaleType;

    /**
     * 售后状态:0 等待买家退货 1 已退货 待收货 2 已收货 换货中 3 系统退款中 4 已完成 5 已取消
     */
    @Column(name = "after_sale_sts")
    private Integer afterSaleSts;

    /**
     * 退款状态:0 同意退款 1 拒绝退款 2 取消退款
     */
    @Column(name = "refund_sts")
    private Integer refundSts;

    /**
     * 售后说明
     */
    @Column(name = "after_sale_remark")
    private String afterSaleRemark;

    /**
     * 评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评
     */
    @Column(name = "evaluate_sts")
    private Integer evaluateSts;

    /**
     * 店铺评分
     */
    @Column(name = "store_score")
    private BigDecimal storeScore;

    /**
     * 技师评分
     */
    @Column(name = "technician_score")
    private BigDecimal technicianScore;

    /**
     * 服务状态： 0未服务 1已服务
     */
    @Column(name = "service_sts")
    private Integer serviceSts;

    /**
     * 主商品uuid
     */
    @Column(name = "goods_uuid")
    private String goodsUuid;

    /**
     * 主商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 主商品数量
     */
    @Column(name = "goods_num")
    private Integer goodsNum;

    /**
     * 主商品图片url
     */
    @Column(name = "goods_img_url")
    private String goodsImgUrl;

    /**
     * 技师uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 技师姓名
     */
    @Column(name = "technician_name")
    private String technicianName;

    /**
     * 技师手机号
     */
    @Column(name = "technician_mobile")
    private String technicianMobile;

    /**
     * 车辆进店里程数
     */
    @Column(name = "car_in_mileage")
    private String carInMileage;

    /**
     * 车辆出店里程数
     */
    @Column(name = "car_out_mileage")
    private String carOutMileage;

    /**
     *确认人类型0：车主，1：店铺/技师
     */
    @Column(name = "confirm_type")
    private Integer confirmType;

    /**
     * 平台服务费用
     *
     */
    @Column(name = "platform_service_money")
    private BigDecimal platformServiceMoney;

}
