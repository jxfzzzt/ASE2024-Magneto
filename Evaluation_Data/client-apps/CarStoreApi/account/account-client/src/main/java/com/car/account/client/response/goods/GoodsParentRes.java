package com.car.account.client.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/23
 */
@Data
@ApiModel(value="GoodsParentRes",description="商品分组VO")
public class GoodsParentRes {

    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "商品分组名称",name = "groupName")
    private String groupName;

    @ApiModelProperty(value = "商品分组英文名称",name = "groupNameEn")
    private String groupNameEn;

    @ApiModelProperty(value = "父组id",name = "parentId")
    private String parentId;

    @ApiModelProperty(value = "排序",name = "orderNum")
    private Integer orderNum;

    @ApiModelProperty(value = "平台补贴",name = "orderNum")
    private BigDecimal sysSubsidy;
}
