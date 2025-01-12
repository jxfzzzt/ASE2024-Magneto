package com.car.account.client.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangyp
 * @date 2021/1/17 20:56
 */
@Data
@ApiModel
public class CalGoodsReq {

    @NotEmpty
    @ApiModelProperty("商品uuid")
    private String goodsUuid;

    @ApiModelProperty("收货地址uuid")
    private String receiveAddrUuid;
}
