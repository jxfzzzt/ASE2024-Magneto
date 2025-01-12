package com.car.account.web.controller.technician;

import com.car.account.client.request.technician.AddAndUpdateTechnicianAccountReq;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.technician.TechnicianAccountRes;
import com.car.account.web.service.technician.TechnicianAccountService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/1
 */
@Slf4j
@Api(value = "TechnicianAccountController", tags = "技师账户信息")
@RequestMapping(value = "/technicianAccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class TechnicianAccountController {

    @Autowired
    private TechnicianAccountService technicianAccountService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryTechnicianAccountInfo", method = RequestMethod.GET)
    @ApiOperation(value = "查询技师账户信息", nickname = "queryTechnicianAccountInfo")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.SELECT, operDesc = "查询技师账户信息")
    public ResultRes<TechnicianAccountRes> queryTechnicianAccountInfo() {
        return technicianAccountService.queryTechnicianAccountInfo();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/addAndUpdateTechnicianAccount", method = RequestMethod.POST)
    @ApiOperation(value = "新增/修改技师账户信息", nickname = "addAndUpdateTechnicianAccount")
    @SysOperLog(operModul = "技师管理", operType = OperEnum.ADD, operDesc = "新增/修改技师账户信息")
    public ResultRes<String> addAndUpdateTechnicianAccount(@RequestBody AddAndUpdateTechnicianAccountReq req) {
        return technicianAccountService.addAndUpdateTechnicianAccount(req);
    }

    @RequestMapping(value = "/updateTechnicianAccount", method = RequestMethod.POST)
    @SysOperLog(operModul = "技师管理", operType = OperEnum.UPDATE, operDesc = "修改技师账户信息")
    @ApiIgnore
    public ResultRes<String> updateTechnicianAccount (@RequestBody @Valid UpdateTechnicianAccountReq req) {
        return technicianAccountService.updateTechnicianAccount(req);
    }
}
