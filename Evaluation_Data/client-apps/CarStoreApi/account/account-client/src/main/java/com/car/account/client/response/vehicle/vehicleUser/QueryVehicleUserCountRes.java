package com.car.account.client.response.vehicle.vehicleUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class QueryVehicleUserCountRes {
    @ApiModelProperty(value = "注册数",name = "registerCount")
    private Integer registerCount;

    @ApiModelProperty(value = "车主数",name = "ownerCount")
    private Integer ownerCount;



}
