package com.car.account.client.response.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class TechnicianCountRes {

    @ApiModelProperty(value = "注册用户数",name = "registerCount")
    private String registerCount;

    @ApiModelProperty(value = "订单总数",name = "orderCount")
    private String orderCount;

    @ApiModelProperty(value = "案例总数",name = "caseCount")
    private String caseCount;

    @ApiModelProperty(value = "问答总数",name = "qaCount ")
    private String qaCount;

    @ApiModelProperty(value = "现场支持总数",name = "supportCount ")
    private String supportCount;



}
