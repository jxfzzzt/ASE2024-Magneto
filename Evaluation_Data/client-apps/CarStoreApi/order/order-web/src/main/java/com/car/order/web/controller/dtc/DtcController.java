package com.car.order.web.controller.dtc;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcReq;
import com.car.order.client.request.dtc.CheckDtcReq;
import com.car.order.client.request.dtc.QueryDtcListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.web.service.dtc.DtcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Slf4j
@RestController
@RequestMapping("/dtc")
@Api(value = "DtcController", tags = "DTC故障管理")
public class DtcController {

    @Autowired
    private DtcService dtcService;


    @GetMapping("/getById/{uuid}")
    @ApiOperation(value = "根据id查询详情", nickname = "getById")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.SELECT, operDesc = "根据id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<QueryDtcInfoRes> getById(@PathVariable(name = "uuid" ,required = true) String uuid) {
        return dtcService.getById(uuid);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增DTC信息", nickname = "add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.ADD, operDesc = "新增DTC信息")
    public ResultRes<String> add(@RequestBody @Validated AddDtcReq req) {
        return dtcService.add(req);
    }

    @PutMapping("/updateById/{uuid}")
    @ApiOperation(value = "根据id修改dtc信息", nickname = "updateById")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.UPDATE, operDesc = "根据id修改dtc信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<String> updateById(@RequestBody @Validated AddDtcReq addDtcReq, @PathVariable(value = "uuid") String uuid) {
        return dtcService.updateById(addDtcReq, uuid);
    }

    @DeleteMapping(value = "/deleteById/{uuid}")
    @ApiOperation(value = "删除dtc信息", nickname = "deleteById")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.DELETE, operDesc = "删除dtc信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    public ResultRes<String> deleteById(@PathVariable("uuid") String uuid) {
        return dtcService.deleteById(uuid);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页列表", nickname = "list")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryDtcListRes>> list(@RequestBody QueryDtcListReq req) {
        return dtcService.list(req);
    }

    @RequestMapping(value = "/checkDtc",method = RequestMethod.POST)
    @ApiOperation(value = "DTC审核", nickname = "checkDtc")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public  ResultRes<String> checkDtc(@RequestBody CheckDtcReq req) {
        return dtcService.checkDtc(req);
    }




    @RequestMapping(value = "/batchImport",method = RequestMethod.POST)
    @ApiOperation(value = "批量导入", nickname = "list")
    @SysOperLog(operModul = "DTC管理", operType = OperEnum.UPLOAD, operDesc = "批量导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes batchImport(@RequestParam("file") MultipartFile file,String brandUuid,String dtcTypeUuid) {
        return dtcService.batchImport(file,brandUuid,dtcTypeUuid);
    }

}
