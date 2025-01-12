package com.car.account.client.request.vehicle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-08 19:21
 */

@Data
@ApiModel(value="AddVehicleTestingReq",description="新增检测Vo")
public class AddVehicleTestingReq {


    @NotBlank(message = "请输入车主id！")
    @ApiModelProperty("车主id")
    private String vehicleUserId ;

    @NotBlank(message = "请输入技师id！")
    @ApiModelProperty("技师id")
    private String technicianUuid ;

    @ApiModelProperty("意见")
    private String remarks;

    @ApiModelProperty(value = "检测明细",name = "detailList")
    List<VehicleTestingDetailReq> list;






}