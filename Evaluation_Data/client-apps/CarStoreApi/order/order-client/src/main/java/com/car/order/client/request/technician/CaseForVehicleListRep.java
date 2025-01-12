package com.car.order.client.request.technician;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2021/1/23
 */
@ApiModel
@Data
public class CaseForVehicleListRep extends PageReq {

    @ApiModelProperty(value="品牌uuid",name="brandUuid")
    private String brandUuid;

    @ApiModelProperty(value="车型",name="model")
    private String model;

    @ApiModelProperty(value="所属系统",name="attachSys")
    private String attachSys;
}
