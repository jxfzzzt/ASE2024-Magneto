package com.car.account.web.controller.vehicle;

import com.car.account.client.request.vehicle.AddVehicleTestingReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleTesting;
import com.car.account.web.service.vehicle.VehicleTestingService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-08 19:13
 */
@Slf4j
@Api(value = "VehicleTestingController", tags = "检查车辆")
@RequestMapping(value = "/vehicletesting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class VehicleTestingController {

    @Autowired
    private VehicleTestingService vehicleTestingService;
    /**
     * 添加车辆检查
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @PostMapping(value = "/addVehicleTesting")
    @ApiOperation(value = "添加车辆检查", nickname = "queryVehicleUserCount")
    @SysOperLog(operModul = "添加车辆检查", operType = OperEnum.SELECT, operDesc = "添加车辆检查")
    public ResultRes<String> addVehicleTesting(@RequestBody @Valid AddVehicleTestingReq addVehicleTestingRes) {
        return vehicleTestingService.addVehicleTesting( addVehicleTestingRes);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @GetMapping(value = "/queryVehicleTesting")
    @ApiOperation(value = "查询车辆检查", nickname = "queryVehicleUserCount")
    @SysOperLog(operModul = "查询车辆检查", operType = OperEnum.SELECT, operDesc = "添加车辆检查")
    public ResultRes<QueryVehicleTesting> queryVehicleTesting(){
        return vehicleTestingService.queryVehicleTesting();
    }

}