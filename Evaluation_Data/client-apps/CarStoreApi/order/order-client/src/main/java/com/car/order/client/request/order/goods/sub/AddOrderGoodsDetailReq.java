package com.car.order.client.request.order.goods.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@ApiModel(value="AddOrderGoodsDetailReq",description="新增订单商品详情请求VO")
public class AddOrderGoodsDetailReq {

    @NotBlank(message = "请输入商品uuid！")
    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

    @NotBlank(message = "请输入商品名称！")
    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @NotNull(message = "请输入商品数量！")
    @ApiModelProperty(value = "商品数量",name = "goodsNum")
    private Integer goodsNum;

    @NotBlank(message = "请输入商品图片地址！")
    @ApiModelProperty(value = "商品图片地址",name = "goodsImgUrl")
    private String goodsImgUrl;

}
