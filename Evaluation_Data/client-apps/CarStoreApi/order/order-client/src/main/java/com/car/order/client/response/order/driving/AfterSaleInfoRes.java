package com.car.order.client.response.order.driving;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="AfterSaleInfoRes",description="订单售后信息VO")
public class AfterSaleInfoRes {


    @ApiModelProperty(value = "退款状态:0 同意退款 1 拒绝退款 2 取消退款",name = "carOwnerUuid")
    private Integer refundSts;

    @ApiModelProperty(value = "售后原因",name = "afterSaleCause")
    private String afterSaleCause;

    @ApiModelProperty(value = "相关图片",name = "afterSaleImgList")
    private List<String> afterSaleImgList;
}
