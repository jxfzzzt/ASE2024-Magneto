package com.car.order.client.response.order.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@ApiModel(value="OrderGoodsDetailRes",description="订单商品详情VO")
public class OrderGoodsDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "商品数量",name = "goodsNum")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片地址",name = "goodsImgUrl")
    private String goodsImgUrl;

    @ApiModelProperty(value = "工时费",name = "manHourCost")
    private BigDecimal manHourCost;

    @ApiModelProperty(value = "材料费",name = "materialsExpenses")
    private BigDecimal materialsExpenses;


}
