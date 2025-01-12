package com.car.account.web.controller.addr;

import com.car.account.client.request.addr.ModifyAddrReq;
import com.car.account.client.request.addr.ReceiveAddrReq;
import com.car.account.client.response.addr.ReceiveAddrRes;
import com.car.account.web.service.addr.AddrService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Slf4j
@Api(value = "AddrController", tags = "地址管理")
@RequestMapping(value = "/addr", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class AddrController {

    @Resource
    AddrService addrService;

    @PostMapping(value = "/addAddr")
    @ApiOperation(value = "新增地址", nickname = "addAddr")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "地址管理", operType = OperEnum.ADD, operDesc = "新增地址")
    public ResultRes<ReceiveAddrRes> addAddr(@RequestBody @Valid ReceiveAddrReq params){
        ReceiveAddrRes receiveAddrRes = addrService.addAddr(params);
        return ResultRes.success(receiveAddrRes);
    }

    @DeleteMapping(value = "/deleteAddr/{uuid}")
    @ApiOperation(value = "删除地址", nickname = "deleteAddr")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "地址管理", operType = OperEnum.DELETE, operDesc = "删除地址")
    public ResultRes<Void> deleteAddr(@PathVariable(name = "uuid") String uuid){

        addrService.disableAddr(uuid);
        return ResultRes.success();
    }


    @PutMapping(value = "/updateAddr")
    @ApiOperation(value = "修改地址", nickname = "updateAddr")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "地址管理", operType = OperEnum.UPDATE, operDesc = "修改地址")
    public ResultRes<ReceiveAddrRes> updateAddr(@RequestBody @Valid ModifyAddrReq params){

        ReceiveAddrRes receiveAddrRes = addrService.updateAddr(params);
        return ResultRes.success(receiveAddrRes);
    }


    @PostMapping(value = "/listAddr")
    @ApiOperation(value = "查询地址列表", nickname = "listAddr")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "地址管理", operType = OperEnum.SELECT, operDesc = "查询地址列表")
    public ResultRes<List<ReceiveAddrRes>> listAddr(){

        List<ReceiveAddrRes> receiveAddrRes = addrService.listAddr();
        return ResultRes.success(receiveAddrRes);
    }


    @GetMapping(value = "/addrDetail/{uuid}")
    @ApiOperation(value = "查询地址详情", nickname = "addrDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "地址管理", operType = OperEnum.SELECT, operDesc = "查询地址详情")
    public ResultRes<ReceiveAddrRes> addrDetail(@PathVariable(name = "uuid") String uuid){

        ReceiveAddrRes receiveAddrRes = addrService.queryAddrDetail(uuid);
        return ResultRes.success(receiveAddrRes);
    }


}
