package com.car.order.web.controller.vehicle;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.*;
import com.car.order.client.response.order.consult.ConsultInfoListRes;
import com.car.order.client.response.order.consult.ConsultOrderDetailRes;
import com.car.order.client.response.order.consult.ConsultRes;
import com.car.order.client.response.order.consult.OrderConsultInfoListRes;
import com.car.order.web.service.consult.ConsultService;
import com.car.order.web.service.consult.OrderConsultService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 咨询订单
 * @author zhangyp
 * @date 2021/1/24 17:15
 */
@Api(value = "ConsultController", tags = "咨询订单管理")
@RequestMapping(value = "/orderConsult", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ConsultController {

    @Autowired
    OrderConsultService orderConsultService;
    @Autowired
    ConsultService consultService;


    /**
     * 咨询
     * @param addConsultReq
     * @return
     */
    @PostMapping(value = "/consult")
    @ApiOperation(value = "新增咨询订单", nickname = "addConsultOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.ADD, operDesc = "新增咨询订单")
    public ResultRes<String> consult(@RequestBody @Valid AddConsultReq addConsultReq){
        return orderConsultService.addConsultOrder(addConsultReq);
    }

    /**
     * 删除咨询订单
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteConsultOrder/{uuid}")
    @ApiOperation(value = "删除咨询订单", nickname = "deleteConsultOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.DELETE, operDesc = "删除咨询订单")
    public ResultRes<String> deleteConsultOrder(@PathVariable(name = "uuid") String uuid){
        return orderConsultService.deleteConsultOrder(uuid);
    }

    /**
     * 回答咨询订单
     * @param addAnswerConsultReq
     * @return
     */
    @PutMapping(value = "/updateConsultAnswer")
    @ApiOperation(value = "回答咨询订单", nickname = "updateConsultAnswer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.UPDATE, operDesc = "回答咨询订单")
    public ResultRes<String> updateConsultAnswer(@RequestBody @Valid AddAnswerConsultReq addAnswerConsultReq){
        return orderConsultService.updateConsultAnswer(addAnswerConsultReq);
    }

    /**
     * 是否采纳回答结果
     * @param updateAcceptResultReq
     * @return
     */
    @PutMapping(value = "/updateAcceptResult")
    @ApiOperation(value = "是否采纳回答结果", nickname = "updateAcceptResult")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.UPDATE, operDesc = "是否采纳回答结果")
    public ResultRes<String> updateAcceptResult(@RequestBody @Valid UpdateAcceptResultReq updateAcceptResultReq){
        return orderConsultService.updateAcceptResult(updateAcceptResultReq);
    }

    /**
     * 全国咨询订单枪单(技师)
     * @param
     * @return
     */
    @GetMapping(value = "/consultOrderSnapUp/{uuid}")
    @ApiOperation(value = "全国咨询订单枪单（技师）", nickname = "consultOrderSnapUp")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.UPDATE, operDesc = "全国咨询订单枪单（技师）")
    public ResultRes<String> consultOrderSnapUp(@PathVariable("uuid") String uuid){
        return orderConsultService.consultOrderSnapUp(uuid);
    }


    /**
     * 新增旁听订单
     * @param addAuditorReq
     * @return
     */
    @PostMapping(value = "/addAuditorOrder")
    @ApiOperation(value = "新增旁听订单", nickname = "addAuditorOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.ADD, operDesc = "新增旁听订单")
    public ResultRes<String> addAuditorOrder(@RequestBody @Valid AddAuditorReq addAuditorReq){
        return orderConsultService.addAuditorOrder(addAuditorReq);
    }

    /**
     * 新增旁听订单
     * @param addAuditorReq
     * @return
     */
    @PostMapping(value = "/addAuditorOrderTwo")
    @ApiOperation(value = "新增旁听订单", nickname = "addAuditorOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.ADD, operDesc = "新增旁听订单")
    public ResultRes<String> addAuditorOrderTwo(@RequestBody @Valid AddAuditorReq addAuditorReq){
        return orderConsultService.addAuditorOrderTwo(addAuditorReq);
    }

    /**
     * 旁听订单支付
     * @param payConsultReq
     * @return
     */
    @PutMapping(value = "/payAuditorOrder")
    @ApiOperation(value = "旁听订单支付", nickname = "payConsultOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.UPDATE, operDesc = "旁听订单支付")
    public ResultRes<String> payAuditorOrder(@RequestBody @Valid PayConsultReq payConsultReq){
        return orderConsultService.payAuditorOrder(payConsultReq);
    }

    /**
     * 查询咨询列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryConsultList")
    @ApiOperation(value = "查询咨询列表", nickname = "queryConsultList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询订单管理", operType = OperEnum.SELECT, operDesc = "查询咨询列表")
    public PageRes<List<ConsultInfoListRes>> queryConsultList(@RequestBody @Valid QueryConsultListReq param){
        return orderConsultService.queryConsultList(param);
    }

    /**
     * 查询咨询订单列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryOrderConsultList")
    @ApiOperation(value = "查询咨询订单列表", nickname = "queryOrderConsultList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询订单管理", operType = OperEnum.SELECT, operDesc = "查询咨询订单列表")
    public PageRes<List<OrderConsultInfoListRes>> queryOrderConsultList(@RequestBody @Valid QueryOrderConsultListReq param){
        return orderConsultService.queryOrderConsultList(param);
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
    @SysOperLog(operModul = "咨询订单管理", operType = OperEnum.SELECT, operDesc = "查询咨询订单详情")
    public ResultRes<ConsultOrderDetailRes> queryOrderConsultDetail(@PathVariable(name = "uuid") String uuid){
        return orderConsultService.queryOrderConsultDetail(uuid);
    }

    /**
     * 咨询订单信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "咨询订单管理", operType = OperEnum.SELECT, operModul = "咨询订单信息导出")
    @ApiOperation(value = "咨询订单信息导出", nickname = "exportOrderConsultList")
    @RequestMapping(value = "/exportOrderConsultList", method = RequestMethod.POST)
    public void exportOrderConsultList(@RequestBody QueryOrderConsultListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        orderConsultService.exportOrderConsultList(exportReq,response);
    }


    @GetMapping(value = "/queryConsultDetail/{uuid}")
    @ApiOperation(value = "查询咨询详情", nickname = "queryConsultDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "咨询管理", operType = OperEnum.SELECT, operDesc = "查询咨询详情")
    ResultRes<ConsultRes> queryConsultDetail(@ApiParam("咨询uuid") @PathVariable(name = "uuid") String uuid){

        return consultService.queryDetail(uuid);
    }
}
