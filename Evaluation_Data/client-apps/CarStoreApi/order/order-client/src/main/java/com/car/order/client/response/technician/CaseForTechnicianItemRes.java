package com.car.order.client.response.technician;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/1/23
 */
@Data
@ApiModel
public class CaseForTechnicianItemRes {

    @ApiModelProperty(value = "案例收益唯一标识",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "案例唯一标识",name = "uuid")
    private String caseUuid;

    @ApiModelProperty(value = "案例名称",name = "title")
    private String title;

    @ApiModelProperty(value = "故障现象",name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "案例收益",name = "amt")
    private BigDecimal amt;

    @ApiModelProperty(value = "案例销量",name = "num")
    private Integer num;

    @ApiModelProperty(value = "收益类型1维修2案例3问答",name = "profitType")
    private Integer profitType;

}
