package com.car.order.client.request.order.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangyp
 * @date 2021/1/20 0:44
 */
@Data
@ApiModel
public class PreOrderReq {

    @NotEmpty
    @ApiModelProperty("商品ID")
    private String goodsId;

    @ApiModelProperty("快递uuid")
    private String receiveAddrUuid;
}
