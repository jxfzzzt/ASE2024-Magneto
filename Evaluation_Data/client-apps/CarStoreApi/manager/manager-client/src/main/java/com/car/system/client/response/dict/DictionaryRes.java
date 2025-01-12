package com.car.system.client.response.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/3/29 15:45
 */
@Data
@ApiModel
public class DictionaryRes {

    @ApiModelProperty(value="数据ID",name="uuid")
    private String uuid;

    @ApiModelProperty(value="类型",name="lableType")
    private String lableType;

    @ApiModelProperty(value="类型描述",name="lableTypeDesc")
    private String lableTypeDesc;

    @ApiModelProperty(value="编号",name="lableCode")
    private String lableCode;

    @ApiModelProperty(value="数值",name="lableValue")
    private String lableValue;

    @ApiModelProperty(value="描述(页面展示)",name="lableDesc")
    private String lableDesc;

    @ApiModelProperty(value="描述英文",name="lableDescEn")
    private String lableDescEn;

    @ApiModelProperty(value="排序(类型列表排序)",name="sortNum")
    private Integer sortNum;

}
