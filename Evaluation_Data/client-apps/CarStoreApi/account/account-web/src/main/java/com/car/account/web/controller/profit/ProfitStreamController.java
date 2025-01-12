package com.car.account.web.controller.profit;

import com.car.account.client.request.platform.PlatformStreamReq;
import com.car.account.client.request.profit.AddProfitReq;
import com.car.account.web.service.profit.ProfitService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyp
 * @date 2021/1/27 21:27
 */
@Slf4j
@Api(value = "ProfitStreamController", tags = "收益管理")
@RequestMapping(value = "/profitStream", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ProfitStreamController {

    @Autowired
    ProfitService profitService;
    /**
     * 添加平台流水
     * @param addProfitReq
     * @return
     */
    @PostMapping(value = "/addProfit")
    @ApiOperation(value = "添加账户流水", nickname = "addPlatfrom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "添加账户流水", operType = OperEnum.ADD, operDesc = "添加账户流水")
    public ResultRes<String> addProfit(@RequestBody AddProfitReq addProfitReq){

        return  profitService.addProfit(addProfitReq);
    }


}
