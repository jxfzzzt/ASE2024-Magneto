package com.car.order.web.model.driving;

import com.car.common.datasource.model.BaseModelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@Table(name = "order_driving")
public class OrderDriving extends BaseModelInfo {

    /**
     * 订单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 服务类型:0 即时 1 预约
     */
    @Column(name = "service_type")
    private Integer serviceType;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private String orderAmount;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    @Column(name = "order_sts")
    private Integer orderSts;

    /**
     * 订单备注信息
     */
    @Column(name = "order_remark")
    private String orderRemark;

    /**
     * 支付方式 0 微信支付 1 支付宝支付
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 订单结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 出发地
     */
    @Column(name = "start_place")
    private String startPlace;

    /**
     * 目的地
     */
    @Column(name = "end_place")
    private String endPlace;

    /**
     * 技师(代驾)uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 技师(代驾)姓名
     */
    @Column(name = "technician_name")
    private String technicianName;

    /**
     * 技师(代驾)手机号
     */
    @Column(name = "technician_mobile")
    private String technicianMobile;

    /**
     * 车牌号
     */
    @Column(name = "plate_number")
    private String plateNumber;

    /**
     * 车主uuid
     */
    @Column(name = "car_owner_uuid")
    private String carOwnerUuid;

    /**
     * 车主地区
     */
    @Column(name = "car_owner_area")
    private String carOwnerArea;

    /**
     * 车主姓名
     */
    @Column(name = "car_owner_name")
    private String carOwnerName;

    /**
     * 车主手机号
     */
    @Column(name = "car_owner_mobile")
    private String carOwnerMobile;

    /**
     * 支付宝账号
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * 退款状态:0 同意退款 1 拒绝退款 2 取消退款
     */
    @Column(name = "refund_sts")
    private Integer refundSts;

    /**
     * 售后原因
     */
    @Column(name = "after_sale_cause")
    private String afterSaleCause;

    /**
     * 评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评
     */
    @Column(name = "evaluate_sts")
    private Integer evaluateSts;

    /**
     * 技师评分
     */
    @Column(name = "technician_score")
    private BigDecimal technicianScore;
}
