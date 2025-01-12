package com.car.account.client.response.goods.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Data
@ApiModel(value="GoodsImgRes",description="商品图片详情VO")
public class GoodsImgRes {

    @ApiModelProperty(value = "图片地址",name = "imgPath")
    private String imgPath;

    @NotNull(message = "请输入图片类型！")
    @ApiModelProperty(value = "图片类型: 0 主图 1 图片 2描述",name = "imgType")
    private Integer imgType;

    @ApiModelProperty("图片主键")
    private String uuid;

    @ApiModelProperty("商品主键")
    private String goodsUuid;

}
