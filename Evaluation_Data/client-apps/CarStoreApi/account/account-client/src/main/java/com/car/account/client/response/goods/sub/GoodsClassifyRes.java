package com.car.account.client.response.goods.sub;

import com.car.account.client.response.goods.GoodsParentRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/14 0:03
 */
@Data
@ApiModel("商品分类")
public class GoodsClassifyRes extends GoodsParentRes{

    @ApiModelProperty(value = "子分类列表",name = "orderNum")
    private List<GoodsParentRes> subList;
}
