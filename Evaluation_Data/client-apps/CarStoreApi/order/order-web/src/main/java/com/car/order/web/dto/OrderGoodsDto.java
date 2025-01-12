package com.car.order.web.dto;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/24
 */
@Data
public class OrderGoodsDto {

    /**
     * 人员uuid
     */
    private String userUuid;
    /**
     * 店铺uuid
     */
    private String storeUuid;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 服务地区,保存具体地址
     */
    private String serviceArea;

    /**
     * 服务单号
     */
    private String serviceNum;

    /**
     * 实收金额 包含快递费 或服务费
     */
    private BigDecimal actualAmount;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 快递费
     */
    private BigDecimal amtExpress;

    /**
     * 服务费
     */
    private BigDecimal amtService;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    private Integer orderSts;

    /**
     * 订单状态List 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    private List<Integer> orderStsList;

    /**
     * 配送方式
     */
    private Integer deliveryMode;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 订单备注信息
     */
    private String orderRemark;

    /**
     * 退款类型: 0 线上退款 1 线下退款
     */
    private Integer refundType;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 售后原因
     */
    private String afterSaleCause;

    /**
     * 售后类型  0 退款 1 退货退款 2 退换货
     */
    private Integer afterSaleType;

    /**
     * 售后状态:0 等待买家退货 1 已退货 待收货 2 已收货 换货中 3 系统退款中 4 已完成 5 已取消
     */
    private Integer afterSaleSts;

    /**
     * 退款状态:0 同意退款 1 拒绝退款 2 取消退款
     */
    private Integer refundSts;

    /**
     * 售后说明
     */
    private String afterSaleRemark;

    /**
     * 评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评
     */
    private Integer evaluateSts;

    /**
     * 店铺评分
     */
    private BigDecimal storeScore;

    /**
     * 技师评分
     */
    private BigDecimal technicianScore;

    /**
     * 服务状态： 0未服务 1已服务
     */
    private Integer serviceSts;
}
