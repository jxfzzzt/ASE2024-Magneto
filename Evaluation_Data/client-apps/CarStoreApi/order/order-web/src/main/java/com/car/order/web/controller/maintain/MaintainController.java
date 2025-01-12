package com.car.order.web.controller.maintain;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcReq;
import com.car.order.client.request.dtc.QueryDtcListReq;
import com.car.order.client.request.maintain.AddMaintainReq;
import com.car.order.client.request.maintain.QueryMaintainListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.client.response.maintain.QueryMaintainRes;
import com.car.order.web.service.maintain.MaintainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: car-service
 * @description: 养护管理
 * @author: niushuaixiang
 * @create: 2021-03-18 19:08
 */
@Slf4j
@RestController
@RequestMapping("/maintain")
@Api(value = "MaintainController", tags = "养护管理")
public class MaintainController {
    @Autowired
    MaintainService maintainService;

    @PostMapping("/add")
    @ApiOperation(value = "新增养护信息", nickname = "add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "新增养护信息", operType = OperEnum.ADD, operDesc = "新增养护信息")
    public ResultRes<String> add(@RequestBody @Validated AddMaintainReq req) {
        return maintainService.add(req);
    }

    @GetMapping("/getById/{uuid}")
    @ApiOperation(value = "根据id查询养护信息详情", nickname = "getById")
    @SysOperLog(operModul = "养护管理", operType = OperEnum.SELECT, operDesc = "根据id查询养护信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<QueryMaintainRes> getById(@PathVariable(name = "uuid" ,required = true) String uuid) {
        return maintainService.getById(uuid);
    }
    @DeleteMapping(value = "/deleteById/{uuid}")
    @ApiOperation(value = "删除养护信息", nickname = "deleteById")
    @SysOperLog(operModul = "养护管理", operType = OperEnum.DELETE, operDesc = "删除养护信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    public ResultRes<String> deleteById(@PathVariable("uuid") String uuid) {
        return maintainService.deleteById(uuid);
    }

    @PutMapping("/updateById/{uuid}")
    @ApiOperation(value = "根据id修改养护信息", nickname = "updateById")
    @SysOperLog(operModul = "养护管理", operType = OperEnum.UPDATE, operDesc = "根据id修改养护信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<String> updateById(@RequestBody @Validated AddMaintainReq addMaintainReq, @PathVariable(value = "uuid") String uuid) {
        return maintainService.updateById(addMaintainReq, uuid);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页列表", nickname = "list")
    @SysOperLog(operModul = "养护信息管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryMaintainRes>> list(@RequestBody QueryMaintainListReq req) {
        return maintainService.list(req);
    }
}