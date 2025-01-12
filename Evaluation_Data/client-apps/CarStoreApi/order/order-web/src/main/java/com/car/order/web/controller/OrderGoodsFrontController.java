package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.front.QueryOrderStsNumReq;
import com.car.order.client.request.order.goods.QueryOrderGoodsFrontListReq;
import com.car.order.client.response.order.front.QueryOrderStsNumRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontRes;
import com.car.order.web.service.front.goods.OrderGoodsFrontService;
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
 * @author zhouz
 * @date 2020/12/30
 */
@Slf4j
@Api(value = "OrderGoodsFrontController", tags = "前端-维修保养订单管理")
@RequestMapping(value = "/orderGoodsFront", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderGoodsFrontController {


    @Autowired
    OrderGoodsFrontService orderGoodsFrontService;

    /**
     * 查询维修保养订单列表
     * @param queryStoreListReq
     * @return
     */
    @PostMapping(value = "/queryOrderGoodsList")
    @ApiOperation(value = "查询维修保养订单", nickname = "queryOrderGoodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "维修保养订单管理", operType = OperEnum.SELECT, operDesc = "查询维修保养订单")
    public PageRes<List<OrderGoodsFrontListRes>> queryOrderGoodsList(@RequestBody @Valid QueryOrderGoodsFrontListReq queryStoreListReq){
        return orderGoodsFrontService.queryOrderGoodsList(queryStoreListReq);
    }

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderGoodsDetail/{uuid}")
    @ApiOperation(value = "查询维修保养订单详情", nickname = "queryOrderGoodsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "维修保养订单管理", operType = OperEnum.SELECT, operDesc = "查询维修保养订单详情")
    public ResultRes<OrderGoodsFrontRes> queryOrderGoodsDetail(@PathVariable(name = "uuid") String uuid){
        return orderGoodsFrontService.queryOrderGoodsDetail(uuid);
    }

    /**
     * 查询订单各状态数量
     * @param req
     * @return
     */
    @PostMapping(value = "/queryOrderStsNum")
    @ApiOperation(value = "查询订单各状态数量", nickname = "queryOrderStsNum")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "查询订单各状态数量", operType = OperEnum.SELECT, operDesc = "查询订单各状态数量")
    public ResultRes<QueryOrderStsNumRes> queryOrderStsNum(@RequestBody @Valid QueryOrderStsNumReq req){
        return orderGoodsFrontService.queryOrderStsNum(req);
    }

}
