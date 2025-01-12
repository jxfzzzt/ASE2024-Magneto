package com.car.order.client.request.order.instance;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@ApiModel(value="QueryOrderCaseListReq",description="查询案例订单列表请求VO对象")
@Data
public class QueryOrderCaseListReq extends PageReq {

    @ApiModelProperty(value = "案例名称",name = "caseName")
    private String caseName;

    @ApiModelProperty(value = "订单号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "开始时间 yyyy-MM-dd",name="startDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startDate;

    @ApiModelProperty(value = "结束时间 yyyy-MM-dd",name="endDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "评价状态 0 未评论  1 已评论 2 好评 3 中评 4 差评",name = "evaluateSts")
    private Integer evaluateSts;

    @ApiModelProperty(value = "车主姓名",name = "carOwnerName")
    private String carOwnerName;

    @ApiModelProperty(value = "车主手机号",name = "carOwnerMobile")
    private String carOwnerMobile;

    @ApiModelProperty(value = "技师姓名",name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "技师手机号",name = "technicianMobile")
    private String technicianMobile;

}
