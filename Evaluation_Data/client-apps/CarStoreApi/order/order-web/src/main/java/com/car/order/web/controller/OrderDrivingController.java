package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.driving.QueryOrderDrivingListReq;
import com.car.order.client.response.order.driving.OrderDrivingInfoListRes;
import com.car.order.client.response.order.driving.OrderDrivingInfoRes;
import com.car.order.web.service.driving.OrderDrivingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Api(value = "OrderDrivingController", tags = "代驾订单管理")
@RequestMapping(value = "/orderDriving", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderDrivingController {


    @Autowired
    OrderDrivingService orderDrivingService;

    /**
     * 查询代驾订单列表
     * @param queryOrderDrivingListReq
     * @return
     */
    @PostMapping(value = "/queryOrderDrivingList")
    @ApiOperation(value = "查询代驾订单列表", nickname = "queryOrderDrivingList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "代驾订单管理", operType = OperEnum.SELECT, operDesc = "查询代驾订单列表")
    public PageRes<List<OrderDrivingInfoListRes>> queryOrderGoodsList(@RequestBody @Valid QueryOrderDrivingListReq queryOrderDrivingListReq){
        return orderDrivingService.queryOrderDrivingList(queryOrderDrivingListReq);
    }

    /**
     * 查询代驾订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderDrivingDetail/{uuid}")
    @ApiOperation(value = "查询代驾订单详情", nickname = "queryOrderDrivingDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "代驾订单管理", operType = OperEnum.SELECT, operDesc = "查询代驾订单详情")
    public ResultRes<OrderDrivingInfoRes> queryOrderGoodsDetail(@PathVariable(name = "uuid") String uuid){
        return orderDrivingService.queryOrderDrivingDetail(uuid);
    }

    /**
     * 代驾订单信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "代驾订单管理", operType = OperEnum.SELECT, operModul = "代驾订单信息导出")
    @ApiOperation(value = "代驾订单信息导出", nickname = "exportOrderDrivingList")
    @RequestMapping(value = "/exportOrderDrivingList", method = RequestMethod.POST)
    public void exportStoreList(@RequestBody QueryOrderDrivingListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        orderDrivingService.exportOrderDrivingList(exportReq,response);
    }
}
