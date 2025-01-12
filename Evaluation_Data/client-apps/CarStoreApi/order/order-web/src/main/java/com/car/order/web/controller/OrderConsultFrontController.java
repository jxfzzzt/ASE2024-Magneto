package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.QueryOrderConsultFrontListReq;
import com.car.order.client.response.order.consult.ConsultOrderFrontRes;
import com.car.order.client.response.order.consult.OrderConsultFrontListRes;
import com.car.order.web.service.front.content.ContentFrontService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Api(value = "OrderConsultFrontController", tags = "前端-付费咨询订单管理")
@RequestMapping(value = "/orderConsultFront", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class OrderConsultFrontController {


    @Autowired
    ContentFrontService contentFrontService;

    /**
     * 查询咨询订单列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryOrderConsultList")
    @ApiOperation(value = "查询付费咨询订单列表", nickname = "queryOrderConsultList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "付费咨询订单管理", operType = OperEnum.SELECT, operDesc = "查询付费咨询订单列表")
    public PageRes<List<OrderConsultFrontListRes>> queryOrderConsultList(@RequestBody @Valid QueryOrderConsultFrontListReq param){
        return contentFrontService.queryOrderConsultList(param);
    }

    /**
     * 查询咨询订单详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryOrderConsultDetail/{uuid}")
    @ApiOperation(value = "查询咨询订单详情", nickname = "queryOrderConsultDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "付费咨询订单管理", operType = OperEnum.SELECT, operDesc = "查询咨询订单详情")
    public ResultRes<ConsultOrderFrontRes> queryOrderConsultDetail(@PathVariable(name = "uuid") String uuid){
        return contentFrontService.queryOrderConsultDetail(uuid);
    }

}
