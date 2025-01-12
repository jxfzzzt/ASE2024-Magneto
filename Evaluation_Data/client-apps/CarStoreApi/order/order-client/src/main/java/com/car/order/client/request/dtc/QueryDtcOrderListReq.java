package com.car.order.client.request.dtc;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/19
 */
@Data
@ApiModel(value = "QueryDtcOrderListReq", description = "查询dtc故障订单列表请求信息，接收参数VO")
public class QueryDtcOrderListReq extends PageReq {

    @ApiModelProperty(value = "咨询者姓名",name = "consultantName")
    private String consultantName;

    @ApiModelProperty(value = "咨询者手机号",name = "consultantMobile")
    private String consultantMobile;

    @ApiModelProperty(value = "开始时间 yyyy-MM-dd",name="startDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startDate;

    @ApiModelProperty(value = "结束时间 yyyy-MM-dd",name="endDate",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "发布者姓名",name = "releaseName")
    private String releaseName;

    @ApiModelProperty(value = "发布者手机号",name = "releaseMobile")
    private String releaseMobile;


}
