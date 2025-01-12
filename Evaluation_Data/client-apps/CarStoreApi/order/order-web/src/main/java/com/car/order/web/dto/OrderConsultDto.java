package com.car.order.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/24
 */
@Data
public class OrderConsultDto {
    /**
     * 咨询uuid
     */
    private String consultUuid;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    private Integer orderSts;

    /**
     * 订单状态List 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败
     */
    private List<Integer> orderStsList;

    /**
     * 订单类型 0 订单点评  1 咨询 2 回答 3 案例 4 旁听
     */
    private Integer orderType;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 支付方式 0 微信支付 1 支付宝支付
     */
    private Integer payType;

    /**
     * 车主uuid
     */
    private String carOwnerUuid;

    /**
     * 车主姓名
     */
    private String carOwnerName;

    /**
     * 车主手机号
     */
    private String carOwnerMobile;

    /**
     * 车主头像
     */
    private String carOwnerImgUrl;

    /**
     * 评价状态: 0 未评论  1 已评论
     */
    private Integer evaluateSts;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date evaluateTime;

    /**
     * 技师评分
     */
    private BigDecimal technicianScore;

    /**
     * 服务状态： 0未服务 1已服务
     */
    private Integer serviceSts;

    /**
     * 技师ID
     */
    private String technicianUuid;
}
