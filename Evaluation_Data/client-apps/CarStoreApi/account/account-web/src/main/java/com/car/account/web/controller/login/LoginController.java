package com.car.account.web.controller.login;

import com.car.account.client.request.login.LoginReq;
import com.car.account.client.response.login.LoginRes;
import com.car.account.web.service.person.PersonService;
import com.car.common.res.ResultRes;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.token.LoginToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户登陆
 */
@RestController
@RequestMapping("/login")
@Slf4j
@Api(value = "LoginController", tags = "用户登陆")
public class LoginController {

    @Autowired
    PersonService personService;


    @GetMapping(value = "/getLoginCode/{accountName}/{terminal}")
    @ApiOperation(value = "获取登陆验证码", nickname = "login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountName", value = "用户名", required = true, dataType = "String" ,paramType="path"),
            @ApiImplicitParam(name = "terminal", value = "登陆终端 vehicle：车主  merchant:（技师/店铺）", required = true, dataType = "String" ,paramType="path")
    })
    public ResultRes getLoginCode(@PathVariable(name = "accountName") String accountName,
                                  @PathVariable(name = "terminal") String terminal, HttpServletRequest request) {
        return personService.getLoginCode(accountName,terminal,request);
    }

    @PostMapping(value = "/userLogin")
    @ApiOperation(value = "用户登陆", nickname = "userLogin")
    public ResultRes<LoginRes> userLogin(@RequestBody @Valid LoginReq param, HttpServletRequest request) {
        return personService.userLogin(param,request);
    }

    @PostMapping(value = "/switchRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @ApiOperation(value = "角色切换", nickname = "switchRole")
    public ResultRes<LoginRes> switchRole() {
        return personService.switchRole();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/exitLogin",method = RequestMethod.GET)
    @ApiOperation(value = "退出登录", nickname = "exitLogin")
    public ResultRes exitLogin(){
        return personService.exitLogin();
    }

    /**
     * 获取token对象
     * @return
     */
    @RequestMapping(value = "/getTokenUser",method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @ApiOperation(value = "获取token对象", nickname = "getToken")
    public ResultRes<LoginToken> getTokenUser(){
        return ResultRes.success(TokenHelper.getLoginToken());
    }
}

