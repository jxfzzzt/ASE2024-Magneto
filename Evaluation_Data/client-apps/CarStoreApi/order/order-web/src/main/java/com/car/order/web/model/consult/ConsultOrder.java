package com.car.order.web.model.consult;

import com.car.common.datasource.model.BaseModelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Data
@Table(name = "consult_order")
public class ConsultOrder extends BaseModelInfo {

    /**
     * 咨询uuid
     */
    @Column(name = "consult_uuid")
    private String consultUuid;

    /**
     * 订单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    @Column(name = "order_sts")
    private Integer orderSts;

    /**
     * 订单类型 0 订单点评  1 咨询 2 回答 3 案例 4 旁听
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 应收金额
     */
    @Column(name = "receivable_amount")
    private BigDecimal receivableAmount;

    /**
     * 支付方式 0 微信支付 1 支付宝支付
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 车主uuid
     */
    @Column(name = "car_owner_uuid")
    private String carOwnerUuid;

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
     * 车主头像
     */
    @Column(name = "car_owner_img_url")
    private String carOwnerImgUrl;

    /**
     * 评价状态: 0 未评论  1 已评论
     */
    @Column(name = "evaluate_sts")
    private Integer evaluateSts;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "evaluate_time")
    private Date evaluateTime;

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
     * owneruuid类型 1:车主，2：技师，3:店铺
     */
    @Column(name = "car_owner_type")
    private Integer carOwnerType;


}
