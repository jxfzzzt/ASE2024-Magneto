package com.car.account.client.response.profit.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收益统计
 * @author zhangyp
 * @date 2021/1/28 22:23
 */
@Data
@ApiModel
public class ProfitStreamClassify {


    @ApiModelProperty("流水分类1代驾2维修3案例4回答5商品售卖")
    private Integer classify;

    @ApiModelProperty("金额")
    private BigDecimal amt;

    @ApiModelProperty("提取金额")
    private BigDecimal withdrawAmt;
}
