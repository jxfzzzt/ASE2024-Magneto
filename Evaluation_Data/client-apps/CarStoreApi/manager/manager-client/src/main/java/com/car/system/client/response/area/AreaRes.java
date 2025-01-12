package com.car.system.client.response.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/3/4 15:23
 */
@ApiModel
@Data
public class AreaRes {

    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "地区编码",name = "areaCode")
    private String areaCode;

    @ApiModelProperty(value = "地区名称",name = "areaName")
    private String areaName;

    @ApiModelProperty(value = "地区拼音",name = "areaNameEn")
    private String areaNameEn;

    @ApiModelProperty(value = "地区级别（1:省份province,2:市city）",name = "areaType")
    private Integer areaType;

    @ApiModelProperty(value = "城市编码",name = "cityCode")
    private String cityCode;

    @ApiModelProperty(value = "地区上级编码",name = "parentCode")
    private String parentCode;

    @ApiModelProperty(value = "地区上级ID",name = "parentUuid")
    private String parentUuid;

    @ApiModelProperty(value = "地区首字母",name = "letter")
    private String letter;
}
