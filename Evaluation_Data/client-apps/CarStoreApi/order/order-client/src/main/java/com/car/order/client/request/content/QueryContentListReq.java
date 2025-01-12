package com.car.order.client.request.content;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@ApiModel(value="QueryContentListReq",description="查询内容列表请求VO对象")
@Data
public class QueryContentListReq extends PageReq {

    @ApiModelProperty(value = "订单名称",name = "orderName")
    private String orderName;

    @ApiModelProperty(value = "订单分类:0 订单点评  1 提问 2 回答 3 案例",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;
}
