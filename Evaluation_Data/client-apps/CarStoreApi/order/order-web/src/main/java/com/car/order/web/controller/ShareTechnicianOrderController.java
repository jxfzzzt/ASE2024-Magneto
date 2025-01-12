package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.technicianappointment.QueryShareTechnicianOrderReq;
import com.car.order.client.response.technicianappointment.ShareTechnicianOrderInfoRes;
import com.car.order.client.request.technicianappointment.ShareTechnicianOrderReq;
import com.car.order.client.response.technicianappointment.ShareTechnicianOrderRes;
import com.car.order.web.service.technicianappointment.ShareTechnicianOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 技师预约订单管理
 *
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.web.controller
 * @NAME: TechnicianAppointmentOrderController
 * @DATE: 2021/3/4 20:59
 */
@Slf4j
@Api(value = "ShareTechnicianOrderController", tags = "技师预约订单管理")
@RequestMapping(value = "/shareTechnicianOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ShareTechnicianOrderController {


    @Autowired
    ShareTechnicianOrderService shareTechnicianOrderService;


    @PostMapping(value = "/saveShareTechnicianOrder")
    @ApiOperation(value = "新增预约技师订单", nickname = "saveShareTechnicianOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.ADD, operDesc = "新增技师预约订单")
    public ResultRes<String> saveShareTechnicianOrder(@RequestBody @Valid ShareTechnicianOrderReq req) {
        return shareTechnicianOrderService.saveTechnicianAppointment(req);
    }

    @PostMapping(value = "/queryShareTechnicianOrderList")
    @ApiOperation(value = "查询预约订单列表", nickname = "queryShareTechnicianOrderList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.SELECT, operDesc = "查询预约订单列表")
    public PageRes<List<ShareTechnicianOrderRes>> queryShareTechnicianOrderList(@RequestBody @Validated QueryShareTechnicianOrderReq req) {
        return shareTechnicianOrderService.queryShareTechnicianOrderList(req);
    }

    @GetMapping(value = "/queryShareTechnicianOrder/{uuid}")
    @ApiOperation(value = "查询预约订单详情", nickname = "queryShareTechnicianOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.SELECT, operDesc = "查询预约订单详情")
    public ResultRes<ShareTechnicianOrderInfoRes> queryShareTechnicianOrder(@PathVariable(name = "uuid") String uuid) {
        return shareTechnicianOrderService.queryShareTechnicianOrder(uuid);
    }

    @PutMapping(value = "/receiveShareTechnicianOrder/{uuid}")
    @ApiOperation(value = "技师同意接单", nickname = "receiveShareTechnicianOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.UPDATE, operDesc = "技师接单")
    public ResultRes receiveShareTechnicianOrder(@PathVariable(name = "uuid") String uuid) {
        return shareTechnicianOrderService.receiveShareTechnicianOrder(uuid);
    }


    @PutMapping(value = "/updateShareTechnicianOrder/{uuid}")
    @ApiOperation(value = "完成预约订单状态", nickname = "updateShareTechnicianOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.UPDATE, operDesc = "完成预约订单状态")
    public ResultRes updateShareTechnicianOrder(@PathVariable(name = "uuid") String uuid) {
        return shareTechnicianOrderService.updateShareTechnicianOrder(uuid);
    }

    @PutMapping(value = "/applicationRefundShareTechnicianOrder/{uuid}")
    @ApiOperation(value = "申请退款预约订单", nickname = "applicationRefundShareTechnicianOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "技师预约订单管理", operType = OperEnum.UPDATE, operDesc = "申请退款预约订单")
    public ResultRes applicationRefundShareTechnicianOrder(@PathVariable(name = "uuid") String uuid) {
        return shareTechnicianOrderService.applicationRefundShareTechnicianOrder(uuid);
    }


}
