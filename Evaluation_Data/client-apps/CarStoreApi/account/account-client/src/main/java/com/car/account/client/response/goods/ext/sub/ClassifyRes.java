package com.car.account.client.response.goods.ext.sub;

import com.car.account.client.response.goods.GoodsRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分类
 * @author zhangyp
 * @date 2021/1/17 19:40
 */
@Data
public class ClassifyRes {

    @ApiModelProperty(value = "商品分组名称",name = "groupName")
    private String groupName;

    @ApiModelProperty(value = "商品分组英文名称",name = "groupNameEn")
    private String groupNameEn;

    @ApiModelProperty(value = "父组id",name = "parentId")
    private String parentId;

    @ApiModelProperty(value = "排序",name = "orderNum")
    private Integer orderNum;
    /**
     * 当前分类商品列表
     */
    @ApiModelProperty("当前分类商品列表")
    private List<GoodsRes> goodsList;
}
