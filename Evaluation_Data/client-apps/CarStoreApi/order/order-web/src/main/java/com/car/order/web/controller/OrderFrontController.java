package com.car.order.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.order.web.service.front.order.OrderFrontService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/31
 */
@Slf4j
@Api(value = "OrderFrontController", tags = "前端-订单管理")
@RequestMapping(value = "/orderFront", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderFrontController {

    @Autowired
    private OrderFrontService orderFrontService;
    /**
     * 查询全部订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderInfo/{uuid}")
    @ApiOperation(value = "查询全部订单详情", nickname = "queryOrderGoodsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "前端-订单管理", operType = OperEnum.SELECT, operDesc = "查询全部订单详情")
    public ResultRes<JSONObject> queryOrderInfo(@PathVariable(name = "uuid") String uuid){
        return orderFrontService.queryOrderInfo(uuid);
    }

    @GetMapping(value = "/queryQuizCount/{userUuid}")
    @ApiOperation(value = "查询我的提问数量", nickname = "queryQuizCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "前端-订单管理", operType = OperEnum.SELECT, operDesc = "查询我的提问数量")
    public ResultRes<Integer> queryQuizCount(@PathVariable(name = "userUuid") String userUuid){
        return orderFrontService.queryQuizCount(userUuid);
    }

    @GetMapping(value = "/queryCaseCount/{userUuid}")
    @ApiOperation(value = "查询我的案例数量", nickname = "queryCaseCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "前端-订单管理", operType = OperEnum.SELECT, operDesc = "查询我的案例数量")
    public ResultRes<Integer> queryCaseCount(@PathVariable(name = "userUuid") String userUuid){
        return orderFrontService.queryCaseCount(userUuid);
    }
}
