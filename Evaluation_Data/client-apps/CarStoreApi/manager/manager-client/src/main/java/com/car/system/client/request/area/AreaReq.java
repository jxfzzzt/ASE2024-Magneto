package com.car.system.client.request.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/14 21:31
 */
@Data
@ApiModel
public class AreaReq {

    @ApiModelProperty("地区主键")
    private String uuid;

    @ApiModelProperty("地区编码")
    private String areaCode;

    @ApiModelProperty("地区名字 模糊查询")
    private String areaName;

    @ApiModelProperty("地区名字英文 模糊查询")
    private String areaNameEn;

    @ApiModelProperty("上级区域编码")
    private String parentCode;
    @ApiModelProperty("上级区域主键")
    private String parentUuid;

    @ApiModelProperty("区域首字母")
    private String letter;

    @ApiModelProperty("关键字检索 地区编码,地区名称,区域首字母模糊查询")
    private String keyWords;
}
