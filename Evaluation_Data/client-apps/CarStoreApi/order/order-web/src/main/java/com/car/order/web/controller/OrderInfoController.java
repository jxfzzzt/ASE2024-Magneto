package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.PayConsultReq;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.request.order.order.ConfirmOrderReq;
import com.car.order.client.request.order.order.OrderWhetherAgreeRefundReq;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.web.service.order.OrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/8
 */
@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@Api(value = "OrderInfoController", tags = "订单管理")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;


    @PostMapping(value = "/addOrder")
    @ApiOperation(value = "新增订单", nickname = "addOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.ADD, operDesc = "新增订单")
    public ResultRes<String> addOrder(@RequestBody @Valid AddOrderInfoReq addOrderInfoReq){
        return orderInfoService.addOrder(addOrderInfoReq);
    }

    @PostMapping(value = "/confirmOrder")
    @ApiOperation(value = "订单确认", nickname = "confirmOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.UPDATE, operDesc = "订单确认")
    public ResultRes<String> confirmOrder(@RequestBody @Valid ConfirmOrderReq req){
        return orderInfoService.confirmOrder(req);
    }

    @GetMapping(value = "/orderRefund/{orderUuid}")
    @ApiOperation(value = "订单退款", nickname = "confirmOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.UPDATE, operDesc = "订单退款")
    public ResultRes<String> orderRefund(@PathVariable(value = "orderUuid") String orderUuid){
        return orderInfoService.orderRefund(orderUuid);
    }

    @PostMapping(value = "/orderWhetherAgreeRefund")
    @ApiOperation(value = "是否同意订单退款", nickname = "orderAgreeRefund")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.UPDATE, operDesc = "同意订单退款")
    public ResultRes<String> orderWhetherAgreeRefund(@RequestBody @Valid OrderWhetherAgreeRefundReq req){
        return orderInfoService.orderWhetherAgreeRefund(req);
    }

    @GetMapping("/queryOrderSts/{orderInfoUuid}")
    @ApiOperation(value = "根据id查询订单状态详情", nickname = "queryOrderSts")
    @SysOperLog(operModul = "订单支付管理", operType = OperEnum.SELECT, operDesc = "根据id查询订单状态详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<Boolean> queryOrderSts(@PathVariable(name = "orderInfoUuid" ,required = true) String orderInfoUuid) {
        return orderInfoService.queryOrderSts(orderInfoUuid);
    }
}













