package com.car.order.client.response.content;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@ApiModel(value="ContentDetailRes",description="内容详情VO对象")
@Data
public class ContentDetailRes {


    @ApiModelProperty(value = "内容uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @ApiModelProperty(value = "订单名称",name = "orderName")
    private String orderName;

    @ApiModelProperty(value = "订单分类:0 订单点评  1 提问 2 回答 3 案例",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "用户uuid",name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "具体内容",name = "contentDetail")
    private String contentDetail;

    @ApiModelProperty(value = "资源列表",name = "resourcesList")
    private List<String> resourcesList;
}
