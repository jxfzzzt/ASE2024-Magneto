package com.car.account.client.request.goods;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Data
@ApiModel(value="QueryGoodsListReq",description="查询商品列表请求VO")
public class QueryGoodsListReq extends PageReq {

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "店铺类型,对应字典表 uuid",name = "storeType")
    private String storeType;

    @ApiModelProperty(value = "一级分类",name = "levelOne")
    private String levelOne;

    @ApiModelProperty(value = "二级分类",name = "levelTwo")
    private String levelTwo;

    @ApiModelProperty(value = "商品类型,对应字典表 uuid",name = "goodsType")
    private String goodsType;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺主键",name = "storeUuid")
    private String storeUuid;

    @Min(value = 0,message = "状态取值区间[0,1]")
    @Max(value = 1,message = "状态取值区间[0,1]")
    @ApiModelProperty(value = "销售状态0 库存(下架) 1 在售(上架)",name = "storeUuid")
    private Integer sellSts;

    @ApiModelProperty(value = "最大销量",name = "maxSalesNum")
    private Integer maxSalesNum;

    @ApiModelProperty(value = "最小销量",name = "minSalesNum")
    private Integer minSalesNum;

    @ApiModelProperty(value = "最大价格",name = "maxPrice")
    private String maxPrice;

    @ApiModelProperty(value = "最小价格",name = "minPrice")
    private String minPrice;

}
