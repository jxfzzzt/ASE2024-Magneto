package com.car.account.client.request.goods;

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
@ApiModel(value="GoodsImgReq",description="新增图片请求VO")
public class GoodsImgReq {

    @NotBlank(message = "请输入图片地址！")
    @ApiModelProperty(value = "图片地址",name = "imgPath")
    private String imgPath;

    @NotNull(message = "请输入图片类型！")
    @ApiModelProperty(value = "图片类型: 0 主图 1 其他",name = "imgType")
    @Max(value = 1, message = "图片类型数值不能大于1")
    @Min(value = 0, message = "图片类型数值不能小于0")
    private Integer imgType;
}
