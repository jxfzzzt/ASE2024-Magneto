package com.car.order.client.response.content;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@ApiModel(value="QueryContentListRes",description="内容列表VO对象")
@Data
public class QueryContentListRes {


    @ApiModelProperty(value = "内容uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单名称",name = "orderName")
    private String orderName;

    @ApiModelProperty(value = "订单分类:0 订单点评  1 提问 2 回答 3 案例",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "具体内容",name = "contentDetail")
    private String contentDetail;
}
