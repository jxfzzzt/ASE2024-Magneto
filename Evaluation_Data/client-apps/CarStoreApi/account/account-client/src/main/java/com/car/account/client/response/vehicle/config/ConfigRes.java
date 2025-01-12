package com.car.account.client.response.vehicle.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class ConfigRes {
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "配置名称",name = "configName")
    private String configName;

    @ApiModelProperty(value = "父类编码，根节点默认：-1",name = "parentCode")
    private String parentCode;

    @ApiModelProperty(value = "配置类型 1：车辆类型 2：车辆品牌 3：车辆型号",name = "configType")
    private String configType;

    @ApiModelProperty(value = "排序",name = "sortNum")
    private Integer sortNum;

}
