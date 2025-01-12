package com.car.account.web.controller.platform;

import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.platform.SelectPlatformReq;
import com.car.account.client.response.platform.PlatformStreamRes;
import com.car.account.web.service.platform.PlatformService;
import com.car.common.annotation.SysOperLog;
import com.car.common.datasource.model.BaseModelInfo;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 
 *
 * @author niushuaixiang
 * @createDate 2021-01-27
 */
@Slf4j
@Api(value = "PlatformController", tags = "平台收益管理")
@RequestMapping(value = "/platform", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class PlatformController  {

    @Autowired
    PlatformService platformService;
    /**
     * 添加平台流水
     * @param platformStreamReq
     * @return
     */
    @PostMapping(value = "/addPlatfrom")
    @ApiOperation(value = "添加平台流水", nickname = "addPlatfrom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "添加平台流水", operType = OperEnum.ADD, operDesc = "添加平台流水")
    public ResultRes<String> addPlatfrom(@RequestBody PlatformStreamReq platformStreamReq){

        return  platformService.addPlatfrom(platformStreamReq);
    }


    /**
     * 根据订单uuid和类型查询
     * @param selectPlatformReq
     * @return
     */
    @PostMapping(value = "/selectPlatfrom")
    @ApiOperation(value = "根据订单uuid和类型查询", nickname = "addPlatfrom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "根据订单uuid和类型查询", operType = OperEnum.ADD, operDesc = "添加平台流水")
    public ResultRes<PlatformStreamRes> selectPlatfrom(@RequestBody SelectPlatformReq selectPlatformReq){

        return  platformService.selectPlatfrom(selectPlatformReq);
    }
}