package com.car.account.client.response.goods.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangyp
 * @date 2021/1/10 17:15
 */
@Data
@ApiModel
public class GoodsDetailRes {

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

    @ApiModelProperty("主键")
    private String uuid;
}
