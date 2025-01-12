package com.car.account.web.controller.vehicle;


import com.car.account.client.request.vehicle.VehicleUserListReq;
import com.car.account.client.request.vehicle.VehicleUserReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryOrderAmountRes;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleUserCountRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.account.web.service.vehicle.VehicleUserService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 车主管理
 * @author xlj
 */
@Slf4j
@Api(value = "VehicleUserController", tags = "车主管理")
@RequestMapping(value = "/vehicleUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class VehicleUserController {

    @Autowired
    VehicleUserService vehicleUserService;

    /**
     * 查询用户数（注册用户/车主数）
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryVehicleUserCount", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户数（注册用户/车主数）", nickname = "queryVehicleUserCount")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "查询用户数（注册用户/车主数）")
    public ResultRes<QueryVehicleUserCountRes> queryVehicleUserCount() {
        return vehicleUserService.queryVehicleUserCount();
    }

    /**
     * 查询车主信息列表
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "查询车主信息列表", nickname = "queryList")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "查询车主信息列表")
    public PageRes<List<VehicleUserRes>> queryList(@RequestBody VehicleUserListReq req) {
        return vehicleUserService.queryList(req);
    }

    /**
     * 查询车主详情
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryDetail/{uuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询车主详情", nickname = "queryDetail")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "查询车主详情")
    public ResultRes<VehicleUserRes> queryDetail(@PathVariable("uuid") String uuid) {
        return vehicleUserService.queryDetail(uuid);
    }

    /**
     * 根据登陆token获取车主详细信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryDetailByToken", method = RequestMethod.GET)
    @ApiOperation(value = "根据登陆token获取车主详细信息", nickname = "queryDetailByToken")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "根据登陆token获取车主详细信息")
    public ResultRes<VehicleUserRes> queryDetailByToken() {
        return vehicleUserService.queryDetailByToken();
    }


    /**
     * 查询车主订单金额简要信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryOrderAmount/{uuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询车主订单金额简要信息", nickname = "queryOrderAmount")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "查询车主订单金额简要信息")
    public ResultRes<QueryOrderAmountRes> queryOrderAmount(@PathVariable("uuid") String uuid) {
        return vehicleUserService.queryOrderAmount(uuid);
    }

    /**
     * 编辑车主信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @ApiOperation(value = "编辑车主信息", nickname = "editVehicleUser")
    @SysOperLog(operModul = "车主管理", operType = OperEnum.SELECT, operDesc = "编辑车主信息")
    public ResultRes editVehicleUser(@RequestBody VehicleUserReq param) {
        return vehicleUserService.editVehicleUser(param);
    }

    /**
     * 车组信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "车主管理", operType = OperEnum.SELECT, operModul = "车组信息导出")
    @ApiOperation(value = "车组信息导出", nickname = "exportVehicleList")
    @RequestMapping(value = "/exportVehicleList", method = RequestMethod.POST)
    public void exportVehicleList(@RequestBody VehicleUserListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        vehicleUserService.exportVehicleList(exportReq,response);
    }

}
