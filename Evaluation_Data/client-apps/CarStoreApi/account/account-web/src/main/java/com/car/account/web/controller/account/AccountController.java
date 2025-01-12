package com.car.account.web.controller.account;

import com.car.account.client.response.account.AccountRes;
import com.car.account.client.response.account.QueryQuizCaseCarCountRes;
import com.car.account.client.response.profit.AccountAmtRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户相关
 * @author zhangyp
 * @date 2021/1/28 22:32
 */
@Slf4j
@Api(value = "AccountController", tags = "账户相关")
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class AccountController {

    @Autowired
    private ProfitService profitService;
    @GetMapping(value = "/account")
    @ApiOperation(value = "账户信息", nickname = "account")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "账户信息", operType = OperEnum.SELECT, operDesc = "账户信息查询")
    public ResultRes<AccountRes> account(){
        return profitService.queryAccount();
    }


    @GetMapping(value = "/classify")
    @ApiOperation(value = "账户信息资金分类", nickname = "classify")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "账户信息资金分类", operType = OperEnum.SELECT, operDesc = "账户信息资金分类")
    public ResultRes<AccountAmtRes> classify(){
        return profitService.queryProfitClassify();
    }

    @GetMapping(value = "/queryQuizCaseCarCount")
    @ApiOperation(value = "查询我的提问、案例、车辆数量", nickname = "queryQuizCaseCarCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "账户相关", operType = OperEnum.SELECT, operDesc = "查询我的提问、案例、车辆数量")
    public ResultRes<QueryQuizCaseCarCountRes> queryQuizCaseCarCount(){
        return profitService.queryQuizCaseCarCount();
    }




}
