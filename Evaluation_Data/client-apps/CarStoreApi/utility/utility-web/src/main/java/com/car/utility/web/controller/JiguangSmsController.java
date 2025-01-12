package com.car.utility.web.controller;

import com.car.common.res.ResultRes;
import com.car.utility.web.service.JiguangSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-07-16 17:10
 */
@RestController
@RequestMapping("/Jiguang")
@Slf4j
@Api(value = "JiguangSmsController", tags = "极光推送")
public class JiguangSmsController {
    @Autowired
    JiguangSmsService jiguangSmsService;

    @RequestMapping(value = "/jpushAll",method = RequestMethod.POST)
    @ApiOperation(value = "统一创建支付订单接口", nickname = "createPayOrder")
    public ResultRes jpushAll(@RequestParam(name = "msg")String msg,@RequestParam(name = "title") String title,@RequestParam(name = "userId") String userId){
        jiguangSmsService.jpushAll(msg,title,userId);
        return ResultRes.success();
    }

}