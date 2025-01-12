package com.car.order.web.model.course;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/2/18
 */
@Data
@Table(name = "course_order")
public class CourseOrder extends BaseModelInfo {

    /**
     * 课程uuid
     */
    @Column(name = "course_uuid")
    private String courseUuid;


    /**
     * 课程类型 0:直播 1:图文
     */
    @Column(name = "course_type")
    private Integer courseType;

    /**
     * 课程讲师
     */
    @Column(name = "course_lecturer")
    private String courseLecturer;


    /**
     * 课程名称
     */
    @Column(name = "course_title")
    private String courseTitle;

    /**
     * 课程封面
     */
    @Column(name = "course_cover")
    private String courseCover;

    /**
     * 订单编号
     */
    @Column(name = "order_num")
    private String orderNum;

    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 支付方式 0 微信支付 1 支付宝支付
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    @Column(name = "order_sts")
    private Integer orderSts;

    /**
     * 购买者uuid
     */
    @Column(name = "buyer_uuid")
    private String buyerUuid;

    /**
     * 购买者姓名
     */
    @Column(name = "buyer_name")
    private String buyerName;

    /**
     * 购买者手机号码
     */
    @Column(name = "buyer_mobile")
    private String buyerMobile;

    /**
     * 评分
     */
    @Column(name = "score")
    private BigDecimal score;
}
