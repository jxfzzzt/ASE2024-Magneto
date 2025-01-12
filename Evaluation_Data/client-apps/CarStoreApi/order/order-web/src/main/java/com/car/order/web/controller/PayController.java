package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.AddAuditorReq;
import com.car.order.client.request.order.consult.PayConsultReq;
import com.car.order.client.request.order.order.PayReq;
import com.car.order.web.service.pay.PayService;
import com.car.utility.client.response.pay.CreateOrderRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/7
 */
@RestController
@RequestMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@Api(value = "PayController", tags = "订单支付管理")
public class PayController {

    @Autowired
    private PayService payService;

    @PostMapping(value = "/payOrder")
    @ApiOperation(value = "订单支付", nickname = "payConsultOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "订单支付管理", operType = OperEnum.UPDATE, operDesc = "订单支付")
    public ResultRes<CreateOrderRes> payConsultOrder(@RequestBody @Valid PayReq req, HttpServletRequest request){
        return payService.payConsultOrder(req, request);
    }

    @GetMapping(value = "/updateOrderPaySts/{orderInfoUuid}")
    @SysOperLog(operModul = "订单支付管理", operType = OperEnum.UPDATE, operDesc = "修改订单支付状态")
    @ApiIgnore
    public ResultRes<String> updateOrderPaySts(@PathVariable(value = "orderInfoUuid") String orderInfoUuid){
        return payService.updateOrderPaySts(orderInfoUuid);
    }


}
