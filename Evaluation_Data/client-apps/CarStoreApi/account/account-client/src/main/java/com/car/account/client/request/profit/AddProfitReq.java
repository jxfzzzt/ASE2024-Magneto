package com.car.account.client.request.profit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 流水查询
 * @author zhangyp
 * @createDate 2021-01-27
 */
@Data
@ApiModel
public class AddProfitReq {
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
     * 金额
     */
    @ApiModelProperty(value="金额",name="amt")
    private BigDecimal amt;
    /**
     * 用户类型1车主2技师3店铺
     */
    @ApiModelProperty(value="用户类型",name="amt")
    private Integer  userType;
    /**
     * 流水类型0收入1支出
     */
    @ApiModelProperty(value="流水类型0收入1支出",name="streamType")
    private Integer streamType;

    /**
     *流水分类
     */
    @ApiModelProperty(value="流水分类1代驾2维修3案例4回答5商品售卖",name="classify")
    private Integer classify;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注",name="remarks")
    private String remarks;


}