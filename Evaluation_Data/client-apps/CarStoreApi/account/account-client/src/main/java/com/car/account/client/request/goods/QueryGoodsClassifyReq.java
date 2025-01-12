package com.car.account.client.request.goods;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询产品列表
 * @author zhangyp
 * @date 2021/1/14 1:31
 */
@Data
@ApiModel
public class QueryGoodsClassifyReq extends PageReq {

    @ApiModelProperty("区域")
    private String areaUuid;

    @ApiModelProperty("品牌")
    private String brandUuid;

    @ApiModelProperty("一级分类")
    private String levelOneUuid;

    @ApiModelProperty("二级分类")
    private String levelTwoUuid;

    @ApiModelProperty("排序类型")
    private String orderType;

    @ApiModelProperty("店铺Uuid")
    private String storeUuid;
}
