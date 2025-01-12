package com.car.account.web.controller.technician;


import com.car.account.client.request.technician.TechnicianAnswerReq;
import com.car.account.client.request.technician.TechnicianListReq;
import com.car.account.client.request.technician.TechnicianLocationListReq;
import com.car.account.client.request.technician.TechnicianReq;
import com.car.account.client.response.technician.*;
import com.car.account.web.service.technician.TechnicianService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 技师信息管理
 * @author xlj
 */
@Slf4j
@Api(value = "TechnicianController", tags = "技师管理")
@RequestMapping(value = "/technician", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class TechnicianController {

    @Autowired
    TechnicianService technicianService;
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册技师", nickname = "queryTechnicianList")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.SELECT, operDesc = "注册技师")
    public ResultRes<String> register(@RequestBody @Valid TechnicianReq param) {
        return technicianService.addTechnician(param);
    }

    /**
     * 删除技师
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteTechnician/{uuid}")
    @ApiOperation(value = "删除技师", nickname = "deleteTechnician")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "技师管理", operType = OperEnum.DELETE, operDesc = "删除技师")
    public ResultRes<String> deleteTechnician(@PathVariable(name = "uuid") String uuid){
        return technicianService.deleteTechnician(uuid);
    }

    /**
     * 查询用户数（注册用户/车主数）
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryTechnicianCount", method = RequestMethod.POST)
    @ApiOperation(value = "查询技师统计数据（注册/订单/案例/回答/支持）", nickname = "queryTechnicianCount")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.SELECT, operDesc = "查询技师统计数据（注册/订单/案例/回答/支持）")
    public ResultRes<TechnicianCountRes> queryTechnicianCount(@RequestBody TechnicianListReq param) {
        return technicianService.queryTechnicianCount(param);
    }

    /**
     * 查询技师信息列表
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryTechnicianList", method = RequestMethod.POST)
    @ApiOperation(value = "查询技师信息列表", nickname = "queryTechnicianList")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.SELECT, operDesc = "查询技师信息列表")
    public PageRes<List<TechnicianListRes>> queryTechnicianList(@RequestBody TechnicianListReq param) {
        return technicianService.queryTechnicianList(param);
    }

    /**
     * 查询布点技师信息列表
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryTechnicianLocationList", method = RequestMethod.POST)
    @ApiOperation(value = "查询技师布点信息列表", nickname = "queryTechnicianLocaltionList")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.SELECT, operDesc = "查询技师布点信息列表")
    public ResultRes<List<TechnicianLocationListRes>> queryTechnicianLocationList(@RequestBody TechnicianLocationListReq req) {
        return technicianService.queryTechnicianLocationList(req);
    }


    /**
     * 技师信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.DOWNLOAD, operModul = "技师信息导出")
    @ApiOperation(value = "技师信息导出", nickname = "exportTechnicianList")
    @RequestMapping(value = "/exportTechnicianList", method = RequestMethod.POST)
    public void exportTechnicianList(@RequestBody TechnicianListReq exportReq, HttpServletResponse response) {
        technicianService.exportTechnicianList(exportReq,response);
    }

    /**
     * 查询技师详情
     * @param uuid
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.SELECT, operModul = "查询技师详情")
    @ApiOperation(value = "查询技师详情", nickname = "queryTechnicianDetail")
    @RequestMapping(value = "/queryTechnicianDetail/{uuid}", method = RequestMethod.GET)
    public ResultRes<TechnicianRes> queryTechnicianDetail(@PathVariable("uuid") String uuid) {
        return technicianService.queryTechnicianDetail(uuid);
    }

    /**
     * 查询技师详情
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.SELECT, operModul = "查询技师详情")
    @ApiOperation(value = "查询技师详情", nickname = "queryTechnicianDetail")
    @RequestMapping(value = "/queryTechnicianDetail", method = RequestMethod.GET)
    public ResultRes<TechnicianRes> queryTechnicianDetail() {

        String userUuid = TokenHelper.getUserUuid();
        Integer userType = TokenHelper.getUserType();

        if(UserTypeEnum.technician.getType().equals(userType)){

            return technicianService.queryTechnicianDetail(userUuid);
        }

        log.error("登录用户非技师>>>>userUuid:{},userType:{}",userUuid,userType);
        throw new BusinessException(ResEnum.TECHNICIAN_NOT_EXIST);
    }
    /**
     * 修改技师信息
     * @param param
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.UPDATE, operModul = "修改技师信息")
    @ApiOperation(value = "修改技师信息", nickname = "updateTechnician")
    @RequestMapping(value = "/updateTechnician", method = RequestMethod.PUT)
    public ResultRes updateTechnician(@RequestBody TechnicianReq param) {
        return technicianService.updateTechnician(param);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @ApiOperation(value = "技师问答列表[车主端]", nickname = "queryTechnicianAnswerList")
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.DOWNLOAD, operModul = "技师问答列表")
    @RequestMapping(value = "/queryTechnicianAnswerList", method = RequestMethod.POST)
    public PageRes<List<TechnicianAnswerRes>> queryTechnicianAnswerList(@RequestBody TechnicianAnswerReq params){
        return technicianService.queryTechnicianAnswerList(params);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @ApiOperation(value = "修改技师问答数量", nickname = "updateQaCount",hidden = true)
    @SysOperLog(operDesc = "技师管理", operType = OperEnum.UPDATE, operModul = "修改技师问答数量")
    @RequestMapping(value = "/updateQaCount/{uuid}", method = RequestMethod.PUT)
    public ResultRes<String> updateQaCount(@PathVariable("uuid") String uuid){
        return technicianService.updateQaCount(uuid);
    }

}
