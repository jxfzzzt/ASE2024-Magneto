package com.car.order.web.controller.sence;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.scene.*;
import com.car.order.client.response.scene.QuerySceneOrderInfoRes;
import com.car.order.client.response.scene.QuerySceneOrderListRes;
import com.car.order.web.service.sence.SceneOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 现场订单信息
 *
 * @author cjw
 * @date 2021-02-26 22:08:48
 */


@Slf4j
@Api(value = "SceneOrderController", tags = "现场下单管理")
@RequestMapping(value = "/sceneOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SceneOrderController {

    @Autowired
    private SceneOrderService sceneOrderService;


    @PostMapping(value = "/addSceneOrder")
    @ApiOperation(value = "新增现场订单", nickname = "addSceneOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.ADD, operDesc = "新增现场订单")
    public ResultRes<String> addSceneOrder(@RequestBody @Valid AddSceneOrderReq req){
        return sceneOrderService.addSceneOrder(req);
    }

    @GetMapping(value = "/grabbingOrders/{sceneOrderUuid}")
    @ApiOperation(value = "现场订单抢单", nickname = "grabbingOrders")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.UPDATE, operDesc = "现场订单抢单")
    public ResultRes<String> grabbingOrders(@PathVariable(name = "sceneOrderUuid") String sceneOrderUuid){
        return sceneOrderService.grabbingOrders(sceneOrderUuid);
    }


    @PostMapping(value = "/sceneOrderDescribe")
    @ApiOperation(value = "现场订单--技师提交说明", nickname = "addSceneOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.ADD, operDesc = "技师提交说明")
    public ResultRes<String> sceneOrderDescribe(@RequestBody @Valid SceneOrderDescribeReq req){
        return sceneOrderService.sceneOrderDescribe(req);
    }

    @PostMapping(value = "/sceneOrderConfirm")
    @ApiOperation(value = "现场订单--客户确定", nickname = "addSceneOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.UPDATE, operDesc = "客户确定")
    public ResultRes<String> sceneOrderConfirm(@RequestBody @Valid  SceneOrderConfirmReq req){
        return sceneOrderService.sceneOrderConfirm(req);
    }


    @PostMapping(value = "/sceneSubmitPlan")
    @ApiOperation(value = "现场订单--提交维修方案", nickname = "addSceneOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.UPDATE, operDesc = "提交维修方案")
    public ResultRes<String> sceneSubmitPlan(@RequestBody @Valid AddSceneOrderServiceReq req){
        return sceneOrderService.sceneSubmitPlan(req);
    }



    @PostMapping(value = "/querySceneOrderList")
    @ApiOperation(value = "查询现场订单列表", nickname = "querySceneOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.SELECT, operDesc = "查询现场订单列表")
    public PageRes<List<QuerySceneOrderListRes>> querySceneOrderList(@RequestBody QuerySceneOrderListReq req){
        return sceneOrderService.querySceneOrderList(req);
    }

    @GetMapping(value = "/querySceneOrderInfo/{uuid}")
    @ApiOperation(value = "查询现场订单详情", nickname = "querySceneOrderInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.SELECT, operDesc = "查询现场订单详情")
    public ResultRes<QuerySceneOrderInfoRes> querySceneOrderInfo(@PathVariable(name = "uuid") String uuid){
        return sceneOrderService.querySceneOrderInfo(uuid);
    }




    @GetMapping(value = "/cancelOrder/{sceneOrderUuid}")
    @ApiOperation(value = "取消现场订单", nickname = "completeOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.UPDATE, operDesc = "完成现场订单")
    public ResultRes<String> cancelOrder(@PathVariable(name = "sceneOrderUuid") String sceneOrderUuid){
        return sceneOrderService.cancelOrderOrder(sceneOrderUuid);
    }

    @GetMapping(value = "/completeOrder/{sceneOrderUuid}")
    @ApiOperation(value = "完成现场订单", nickname = "completeOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场订单管理", operType = OperEnum.UPDATE, operDesc = "完成现场订单")
    public ResultRes<String> completeOrder(@PathVariable(name = "sceneOrderUuid") String sceneOrderUuid){
        return sceneOrderService.completeOrder(sceneOrderUuid);
    }




}
