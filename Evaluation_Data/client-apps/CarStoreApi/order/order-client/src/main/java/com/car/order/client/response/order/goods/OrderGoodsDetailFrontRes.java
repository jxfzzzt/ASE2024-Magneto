package com.car.order.client.response.order.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@ApiModel(value="OrderGoodsDetailFrontRes",description="订单商品详情VO")
public class OrderGoodsDetailFrontRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

    @ApiModelProperty(value = "商品明细名称",name = "goodsName")
    private String goodsDetailName;

    @ApiModelProperty(value = "商品明细数量",name = "goodsNum")
    private Integer goodsDetailNum;

    @ApiModelProperty(value = "商品明细单价",name = "goodsDetailAmt")
    private BigDecimal goodsDetailAmt;

    @ApiModelProperty(value = "商品明细实际支付价格",name = "goodsDetailActAmt")
    private BigDecimal goodsDetailActAmt;

}
