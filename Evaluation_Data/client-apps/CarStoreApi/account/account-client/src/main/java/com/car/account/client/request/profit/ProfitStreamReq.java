package com.car.account.client.request.profit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流水查询
 * @author zhangyp
 * @createDate 2021-01-27
 */
@Data
@ApiModel
public class ProfitStreamReq {
    /**
     * 人员uuid
     */
    @ApiModelProperty(value="人员uuid",name="userUuid")
    private String userUuid;

    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号",name="orderNo")
    private String orderNo;


    /**
     * 流水类型0收入1支出
     */
    @ApiModelProperty(value="流水类型0收入1支出",name="streamType")
    private Integer streamType;


    @ApiModelProperty(value="流水分类1代驾2维修3案例4回答5商品售卖",name="classify")
    private String classify;


}