package com.car.account.client.response.goods;

import com.car.account.client.response.addr.ReceiveAddrRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 计算商品
 * @author zhangyp
 * @date 2021/1/17 20:51
 */
@Data
@ApiModel
public class CalGoodsRes {

    @ApiModelProperty("商品信息")
    private GoodsRes goodsRes;

    @ApiModelProperty("收货信息")
    private ReceiveAddrRes receiveAddrRes;
}
