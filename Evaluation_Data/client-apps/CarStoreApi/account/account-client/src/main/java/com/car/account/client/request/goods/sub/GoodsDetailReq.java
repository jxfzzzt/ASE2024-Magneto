package com.car.account.client.request.goods.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品明细
 * @author zhangyp
 * @date 2021/1/9 18:59
 */
@Data
@ApiModel
public class GoodsDetailReq {

    @ApiModelProperty("物料名称")
    private String name;

    @ApiModelProperty("价格")
    private BigDecimal actAmt;

    @ApiModelProperty("备用字段1")
    private String bak1;

    @ApiModelProperty("备用字段2")
    private String bak2;

    @ApiModelProperty("备用字段3")
    private String bak3;

    @ApiModelProperty("备用字段4")
    private String bak4;

}
