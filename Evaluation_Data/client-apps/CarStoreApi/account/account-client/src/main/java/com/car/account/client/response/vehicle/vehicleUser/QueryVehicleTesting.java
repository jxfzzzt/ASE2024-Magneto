package com.car.account.client.response.vehicle.vehicleUser;

import com.car.account.client.request.vehicle.VehicleTestingDetailReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-09 11:11
 */
@Data
@ApiModel
public class QueryVehicleTesting {


    @ApiModelProperty("车主id")
    private String vehicleUserId ;


    @ApiModelProperty("技师id")
    private String technicianUuid ;

    @ApiModelProperty("意见")
    private String remarks;
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content ;



}