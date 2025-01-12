package com.car.utility.web.controller;

import com.car.common.res.ResultRes;
import com.car.utility.web.service.SendMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件发送服务
 * @author xlj
 */
@RestController
@RequestMapping("/email")
@Slf4j
@Api(value = "EmailController", tags = "邮件发送")
public class EmailController {

    @Autowired
    SendMailService sendMailService;



    /**
     * 登陆确认验证码
     * @return
     */
    @PostMapping(value = "sendLogin")
    @ApiOperation(value = "登陆确认验证码", nickname = "sendLogin")
    public ResultRes<String> sendLogin(@RequestParam("email") String email, @RequestParam("code") String code) {
        log.info("EMAIL----->登陆确认验证码--->email:{}----->code:{}",email,code);
        sendMailService.sendLogin(email,code);
        return ResultRes.success();
    }


}

