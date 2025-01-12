package com.car.account.client.request.platform;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 *
 * @author niushuaixiang
 * @createDate 2021-01-27
 */
@Data
@ApiModel
public class PlatformStreamReq  {

    /**
     * 订单号
     */
    @ApiModelProperty(value="订单号",name="orderNo")
    private String orderNo;

    /**
     * 金额
     */
    @ApiModelProperty(value="金额",name="amt")
    private BigDecimal amt;

    /**
     * 流水类型0收入1支出
     */
    @ApiModelProperty(value="流水类型0收入1支出",name="streamType")
    private Integer streamType;

    /**
     * 流水分类 1:平台服务费2：入账金额
     */
    @ApiModelProperty(value="流水分类0",name="classify")
    private Integer classify;


    /**
     * 订单分类0 订单点评  1 咨询 2 回答 3 案例 4 旁听 5 维修保养 6 代驾 7 dtc故障 8 课程 10：共享技师
     */
    @ApiModelProperty(value="订单分类0",name="orderType")
    private Integer orderType;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注",name="remarks")
    private String remarks;
}