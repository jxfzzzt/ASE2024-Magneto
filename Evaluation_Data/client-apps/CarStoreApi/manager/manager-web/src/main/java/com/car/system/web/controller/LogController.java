package com.car.system.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.req.SysOperationLogReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.log.QueryLogListReq;
import com.car.system.client.response.log.LogRes;
import com.car.system.web.service.SysOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Buerger
 * @date 2020/5/26 16:25
 */
@Slf4j
@Api(value = "LogController", tags = "操作日志")
@RequestMapping(value = "/log")
@RestController
public class LogController {

    @Autowired
    SysOperationLogService sysOperationLogService;

    @PostMapping("/queryList")
    @ApiOperation(value = "查询操作日志信息", nickname = "queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    public PageRes<List<LogRes>> queryList(@RequestBody QueryLogListReq param){
        return sysOperationLogService.queryLogList(param);
    }

    @PostMapping("/insertSysLog")
    @ApiOperation(value = "保存系统操作日志", nickname = "insertSysLog")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    public ResultRes<String> insertSysLog(@RequestBody SysOperationLogReq param){
        return sysOperationLogService.insertSysLog(param);
    }
}
