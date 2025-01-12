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
public class QueryOrderCaseFrontListReq extends PageReq {

    @ApiModelProperty(value = "状态 0 全部 1 待付款 2: 待服务 3:待评价  4:退款/售后",name = "state")
    private Integer state;

    @ApiModelProperty(value = "人员类型： 0 车主 ，1 技师")
    private Integer userType;

}
