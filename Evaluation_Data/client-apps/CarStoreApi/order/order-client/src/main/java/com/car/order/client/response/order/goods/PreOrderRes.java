package com.car.order.client.response.order.goods;

import com.car.order.client.response.order.goods.sub.ReceiveAddrRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/20 0:46
 */
@Data
public class PreOrderRes {

    @ApiModelProperty("商品主键")
    private String goodsUuid;

    @ApiModelProperty("商品图片地址列表")
    private List<String> goodsImgList;

    @ApiModelProperty("地址信息")
    private ReceiveAddrRes receiveAddrRes;

    @ApiModelProperty("商品金额")
    private BigDecimal amt;
}
