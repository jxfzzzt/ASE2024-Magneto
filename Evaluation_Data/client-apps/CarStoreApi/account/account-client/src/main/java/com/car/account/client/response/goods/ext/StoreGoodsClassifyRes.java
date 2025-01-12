package com.car.account.client.response.goods.ext;

import com.car.account.client.response.goods.ext.sub.ClassifyRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺商品分类
 * @author zhangyp
 * @date 2021/1/17 19:38
 */
@Data
@ApiModel
public class StoreGoodsClassifyRes extends ClassifyRes {

    @ApiModelProperty("子分类列表")
    private List<ClassifyRes> subList;
}
