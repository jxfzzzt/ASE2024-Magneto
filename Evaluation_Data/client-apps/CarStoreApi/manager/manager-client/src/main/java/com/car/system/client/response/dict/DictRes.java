package com.car.system.client.response.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Buerger
 * @date 2020/5/26 16:26
 */
@Data
@ApiModel
public class DictRes {

    @ApiModelProperty(value = "类型描述")
    private String lableTypeDesc;


    @ApiModelProperty(value = "编号(传递后端值)")
    private String lableCode;


    @ApiModelProperty(value = "数值")
    private String lableValue;

    @ApiModelProperty(value = "类型描述")
    private String lableDesc;


}
