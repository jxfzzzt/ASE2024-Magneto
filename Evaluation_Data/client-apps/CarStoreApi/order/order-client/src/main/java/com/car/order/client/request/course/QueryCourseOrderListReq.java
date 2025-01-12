package com.car.order.client.request.course;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/22
 */
@Data
@ApiModel(value = "QueryCourseOrderListReq", description = "查询课程订单列表返回VO")
public class QueryCourseOrderListReq extends PageReq {

    @ApiModelProperty(value = "课程讲师", name = "courseLecturer")
    private String courseLecturer;

    @ApiModelProperty(value = "下单开始时间 yyyy-MM-dd",name="startDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startDate;

    @ApiModelProperty(value = "下单结束时间 yyyy-MM-dd",name="endDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;
}
