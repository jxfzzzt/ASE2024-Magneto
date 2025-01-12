package com.car.order.client.response.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/22
 */
@Data
@ApiModel(value = "QueryCourseOrderListRes", description = "查询课程订单列表返回VO")
public class QueryCourseOrderListRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "课程uuid", name = "courseUuid")
    private String courseUuid;

    @ApiModelProperty(value = "课程类型 0:直播 1:图文", name = "courseType")
    private Integer courseType;

    @ApiModelProperty(value = "购买者uuid", name = "buyerUuid")
    private String buyerUuid;

    @ApiModelProperty(value = "购买者姓名", name = "buyerName")
    private String buyerName;

    @ApiModelProperty(value = "购买者手机号码", name = "buyerMobile")
    private String buyerMobile;

    @ApiModelProperty(value = "课程名称", name = "courseTitle")
    private String courseTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime ")
    private Date createdTime;

    @ApiModelProperty(value = "课程讲师", name = "courseLecturer")
    private String courseLecturer;

    @ApiModelProperty(value = "订单金额", name = "orderAmount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "课程封面", name = "courseCover")
    private String courseCover;

    @ApiModelProperty(value = "订单编号", name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付", name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败", name = "orderSts")
    private Integer orderSts;

}
