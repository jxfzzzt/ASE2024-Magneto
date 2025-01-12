package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.goods.*;
import com.car.order.client.response.order.goods.OrderGoodsListRes;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.client.response.order.goods.PreOrderRes;
import com.car.order.web.service.goods.OrderGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Slf4j
@Api(value = "OrderGoodsController", tags = "商品订单管理")
@RequestMapping(value = "/orderGoods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderGoodsController {


    @Autowired
    OrderGoodsService orderGoodsService;

    /**
     * 查询商品订单列表
     * @param queryStoreListReq
     * @return
     */
    @PostMapping(value = "/queryOrderGoodsList")
    @ApiOperation(value = "查询商品订单列表", nickname = "queryOrderGoodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品订单管理", operType = OperEnum.SELECT, operDesc = "查询商品订单列表")
    public PageRes<List<OrderGoodsListRes>> queryOrderGoodsList(@RequestBody @Valid QueryOrderGoodsListReq queryStoreListReq){
        return orderGoodsService.queryOrderGoodsList(queryStoreListReq);
    }

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderGoodsDetail/{uuid}")
    @ApiOperation(value = "查询商品订单详情", nickname = "queryOrderGoodsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品订单管理", operType = OperEnum.SELECT, operDesc = "查询商品订单详情")
    public ResultRes<OrderGoodsRes> queryOrderGoodsDetail(@PathVariable(name = "uuid") String uuid){
        return orderGoodsService.queryOrderGoodsDetail(uuid);
    }

    /**
     * 商品订单信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "商品订单管理", operType = OperEnum.SELECT, operModul = "商品订单信息导出")
    @ApiOperation(value = "商品订单信息导出", nickname = "exportOrderGoodsList")
    @RequestMapping(value = "/exportOrderGoodsList", method = RequestMethod.POST)
    public void exportStoreList(@RequestBody QueryOrderGoodsListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        orderGoodsService.exportOrderGoodsList(exportReq,response);
    }

    @PostMapping(value = "/confirmOrder")
    @ApiOperation(value = "确认订单", nickname = "confirmOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.SELECT, operDesc = "确认订单")
    public ResultRes<PreOrderRes> confirmOrder(@RequestBody @Valid PreOrderReq params){

        return orderGoodsService.preOrder(params);
    }
    @PostMapping(value = "/order")
    @ApiOperation(value = "提交订单", nickname = "order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.ADD, operDesc = "下单")
    public ResultRes<String> order(@RequestBody @Valid CreateOrderReq params){

        String orderNo = orderGoodsService.createOrder(params);
        return ResultRes.success(orderNo);
    }

    @PostMapping(value = "/updateGoodsServerOrder")
    @ApiOperation(value = "修改订单服务信息", nickname = "updateGoodsOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.UPDATE, operDesc = "修改订单服务信息")
    public ResultRes<String> updateGoodsServerOrder(@RequestBody @Valid UpdateServerOrderReq req){
        return orderGoodsService.updateGoodsOrder(req);
    }

    @PostMapping(value = "/updateGoodsDeliveryOrder")
    @ApiOperation(value = "修改订单物流信息", nickname = "updateGoodsDeliveryOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单管理", operType = OperEnum.UPDATE, operDesc = "修改订单物流信息")
    public ResultRes<String> updateGoodsDeliveryOrder(@RequestBody @Valid UpdateDeliveryOrder req){
        return orderGoodsService.updateGoodsDeliveryOrder(req);
    }
}
