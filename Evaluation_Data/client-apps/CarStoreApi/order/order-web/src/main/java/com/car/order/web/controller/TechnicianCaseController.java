package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.AddAnswerConsultReq;
import com.car.order.client.request.technician.AddTechnicianCaseReq;
import com.car.order.client.request.technician.CaseForTechnicianListRep;
import com.car.order.client.request.technician.CaseForVehicleListRep;
import com.car.order.client.request.technician.UpdateTechnicianCaseReq;
import com.car.order.client.response.technician.CaseForTechnicianItemRes;
import com.car.order.client.response.technician.CaseForVehicleItemRes;
import com.car.order.client.response.technician.TechnicianCaseRes;
import com.car.order.web.service.technician.TechnicianCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/21
 */
@Slf4j
@Api(value = "TechnicianController", tags = "技师案例管理")
@RequestMapping(value = "/case", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class TechnicianCaseController {

    @Autowired
    private TechnicianCaseService technicianCaseService;

    /**
     * 新增案例
     * @param addTechnicianCaseReq
     * @return
     */
    @PostMapping(value = "/addCase")
    @ApiOperation(value = "新增案例", nickname = "addCase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例管理", operType = OperEnum.ADD, operDesc = "新增案例")
    public ResultRes<String> addCase(@RequestBody @Valid AddTechnicianCaseReq addTechnicianCaseReq){
        return technicianCaseService.addTechnicianCase(addTechnicianCaseReq);
    }


    /**
     * 修改案例
     * @param updateTechnicianCaseReq
     * @return
     */
    @PutMapping(value = "/updateTechnicianCase")
    @ApiOperation(value = "修改案例", nickname = "updateTechnicianCase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例管理", operType = OperEnum.UPDATE, operDesc = "修改案例")
    public ResultRes<String> updateTechnicianCase (@RequestBody @Valid UpdateTechnicianCaseReq updateTechnicianCaseReq){
        return technicianCaseService.updateTechnicianCase(updateTechnicianCaseReq);
    }

    /**
     * 查询案例列表 技师查询
     * @param pageReq
     * @return
     */
    @PostMapping(value = "/queryCaseForTechnicianList")
    @ApiOperation(value = "查询案例列表 技师查询", nickname = "queryCaseForTechnicianList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例管理", operType = OperEnum.SELECT, operDesc = "查询案例列表 技师查询")
    public PageRes<List<CaseForTechnicianItemRes>> queryCaseForTechnicianList(@RequestBody PageReq pageReq){
        CaseForTechnicianListRep caseForTechnicianListRep = new CaseForTechnicianListRep();
        BeanUtils.copyProperties(pageReq,caseForTechnicianListRep);
        return technicianCaseService.queryCaseForTechnicianList(caseForTechnicianListRep);
    }

    /**
     * 查询案例列表 车主查询
     * @param caseForVehicleListRep
     * @return
     */
    @PostMapping(value = "/queryCaseForVehicleList")
    @ApiOperation(value = "查询案例列表 车主查询", nickname = "queryCaseForVehicleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例管理", operType = OperEnum.SELECT, operDesc = "查询案例列表 车主查询")
    public PageRes<List<CaseForVehicleItemRes>> queryCaseForVehicleList(@RequestBody CaseForVehicleListRep caseForVehicleListRep){
        return technicianCaseService.queryCaseForVehicleList(caseForVehicleListRep);
    }

    /**
     * 查询案例详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryCaseDetail/{uuid}")
    @ApiOperation(value = "查询案例详情", nickname = "queryCaseDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例管理", operType = OperEnum.SELECT, operDesc = "查询案例详情")
    public ResultRes<TechnicianCaseRes> queryTechnicianCaseDetail(@PathVariable(name = "uuid") String uuid){
        return technicianCaseService.queryTechnicianCaseDetail(uuid);
    }
}
