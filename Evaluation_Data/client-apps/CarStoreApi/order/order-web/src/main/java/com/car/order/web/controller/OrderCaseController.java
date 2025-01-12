package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.instance.OrderCaseDetailRes;
import com.car.order.client.response.order.instance.OrderCaseInfoListRes;
import com.car.order.web.service.instance.OrderCaseService;
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
@Api(value = "OrderCaseController", tags = "案例订单管理")
@RequestMapping(value = "/orderCase", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderCaseController {

    @Autowired
    OrderCaseService orderCaseService;

    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryOrderCaseList")
    @ApiOperation(value = "查询案例订单列表", nickname = "queryOrderCaseList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例订单管理", operType = OperEnum.SELECT, operDesc = "查询案例订单列表")
    public PageRes<List<OrderCaseInfoListRes>> queryOrderCaseList(@RequestBody @Valid QueryOrderCaseListReq param){
        return orderCaseService.queryOrderCaseList(param);
    }

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderCaseDetail/{uuid}")
    @ApiOperation(value = "查询案例订单详情", nickname = "queryOrderCaseDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例订单管理", operType = OperEnum.SELECT, operDesc = "查询案例订单详情")
    public ResultRes<OrderCaseDetailRes> queryOrderCaseDetail(@PathVariable(name = "uuid") String uuid){
        return orderCaseService.queryOrderCaseDetail(uuid);
    }

    /**
     * 案例订单信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "案例订单管理", operType = OperEnum.SELECT, operModul = "案例订单信息导出")
    @ApiOperation(value = "案例订单信息导出", nickname = "exportOrderCaseList")
    @RequestMapping(value = "/exportOrderCaseList", method = RequestMethod.POST)
    public void exportStoreList(@RequestBody QueryOrderCaseListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        orderCaseService.exportOrderDrivingList(exportReq,response);
    }
}
