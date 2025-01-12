package com.car.account.web.controller.vehicle;


import com.car.account.client.request.vehicle.VehicleReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleUserCountRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleRes;
import com.car.account.web.service.vehicle.VehicleService;
import com.car.account.web.service.vehicle.VehicleUserService;
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

import java.util.List;

/**
 * 车主管理
 * @author xlj
 */
@Slf4j
@Api(value = "VehicleController", tags = "我的车辆")
@RequestMapping(value = "/vehicle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    /**
     * 查询我的车辆列表
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryMyVehicle", method = RequestMethod.GET)
    @ApiOperation(value = "查询我的车辆列表", nickname = "queryMyVehicle")
    @SysOperLog(operModul = "我的车辆", operType = OperEnum.SELECT, operDesc = "查询我的车辆列表")
    public ResultRes<List<VehicleRes>> queryMyVehicle() {
        return vehicleService.queryMyVehicle();
    }

    /**
     * 查询车辆详情接口
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryVehicleDetail/{vehicleUuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询我的车辆详情", nickname = "queryVehicleDetail")
    @SysOperLog(operModul = "我的车辆", operType = OperEnum.SELECT, operDesc = "查询我的车辆列表")
    public ResultRes<VehicleRes> queryVehicleDetail(@PathVariable("vehicleUuid") String vehicleUuid) {
        return vehicleService.queryVehicleDetail(vehicleUuid);
    }


    /**
     * 新增用户车辆信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/addMyVehicle", method = RequestMethod.POST)
    @ApiOperation(value = "新增我的车辆信息", nickname = "addMyVehicle")
    @SysOperLog(operModul = "我的车辆", operType = OperEnum.ADD, operDesc = "新增我的车辆信息")
    public ResultRes<String> addMyVehicle(@RequestBody VehicleReq param) {
        return vehicleService.addMyVehicle(param);
    }

    /**
     * 编辑用户车辆信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/editMyVehicle", method = RequestMethod.PUT)
    @ApiOperation(value = "编辑我的车辆信息", nickname = "editMyVehicle")
    @SysOperLog(operModul = "我的车辆", operType = OperEnum.UPDATE, operDesc = "编辑我的车辆信息")
    public ResultRes<String> editMyVehicle(@RequestBody VehicleReq param) {
        return vehicleService.editMyVehicle(param);
    }

    /**
     * 删除我的车辆信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/deleteMyVehicle/{vehicleUuid}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除我的车辆信息", nickname = "deleteMyVehicle")
    @SysOperLog(operModul = "我的车辆", operType = OperEnum.DELETE, operDesc = "删除我的车辆信息")
    public ResultRes<String> deleteMyVehicle(@PathVariable("vehicleUuid") String vehicleUuid) {
        return vehicleService.deleteMyVehicle(vehicleUuid);
    }

}
