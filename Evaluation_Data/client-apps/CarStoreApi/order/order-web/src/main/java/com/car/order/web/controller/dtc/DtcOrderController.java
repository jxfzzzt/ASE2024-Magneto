package com.car.order.web.controller.dtc;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcOrderReq;
import com.car.order.client.request.dtc.QueryDtcOrderListReq;
import com.car.order.client.response.dtc.QueryDtcOrderInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderListRes;
import com.car.order.web.service.dtc.DtcOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Slf4j
@RestController
@RequestMapping("/dtcOrder")
@Api(value = "DtcOrderController", tags = "DTC故障订单管理")
public class DtcOrderController {

    @Autowired
    private DtcOrderService dtcOrderService;


    @GetMapping("/getById/{uuid}")
    @ApiOperation(value = "根据id查询订单详情", nickname = "getById")
    @SysOperLog(operModul = "DTC故障订单管理", operType = OperEnum.SELECT, operDesc = "根据id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<QueryDtcOrderInfoRes> getById(@PathVariable(name = "uuid" ,required = true) String uuid) {
        return dtcOrderService.getById(uuid);
    }

    @PostMapping("/addOrder")
    @ApiOperation(value = "新增DTC订单信息", nickname = "add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "DTC故障订单管理", operType = OperEnum.ADD, operDesc = "新增DTC信息")
    public ResultRes<String> addOrder(@RequestBody @Validated AddDtcOrderReq req) {
        return dtcOrderService.addOrder(req);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页订单列表", nickname = "list")
    @SysOperLog(operModul = "DTC故障订单管理", operType = OperEnum.SELECT, operDesc = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryDtcOrderListRes>> list(@RequestBody QueryDtcOrderListReq req) {
        return dtcOrderService.list(req);
    }

    @RequestMapping(value = "/myList",method = RequestMethod.POST)
    @ApiOperation(value = "分页查询我的订单列表", nickname = "list")
    @SysOperLog(operModul = "DTC故障订单管理", operType = OperEnum.SELECT, operDesc = "分页查询我的订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public PageRes<List<QueryDtcOrderListRes>> myList(@RequestBody QueryDtcOrderListReq req) {
        return dtcOrderService.myList(req);
    }

    /**
     * DTC订单信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "DTC故障订单管理", operType = OperEnum.SELECT, operModul = "DTC订单信息导出")
    @ApiOperation(value = "DTC订单信息导出", nickname = "exportOrderDtcList")
    @RequestMapping(value = "/exportOrderDtcList", method = RequestMethod.POST)
    public void exportStoreList(@RequestBody QueryDtcOrderListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        dtcOrderService.exportOrderDtcList(exportReq,response);
    }

    @GetMapping("/queryOrderSts/{uuid}")
    @ApiOperation(value = "根据id查询订单状态详情", nickname = "queryOrderSts")
    @SysOperLog(operModul = "DTC故障订单管理", operType = OperEnum.SELECT, operDesc = "根据id查询订单状态详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<Boolean> queryOrderSts(@PathVariable(name = "uuid" ,required = true) String uuid) {
        return dtcOrderService.queryOrderSts(uuid);
    }

}
