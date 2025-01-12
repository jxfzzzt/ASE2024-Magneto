package com.car.order.web.controller.sence;

import java.util.Arrays;
import java.util.Map;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.order.client.request.scene.AddSceneOrderReq;
import com.car.order.client.request.scene.AddSceneTechnicianInfoReq;
import com.car.order.web.service.sence.SceneOrderTechnicianService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 现场订单技师相关
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2021-02-26 22:08:47
 */
@Slf4j
@Api(value = "SceneOrderTechnicianController", tags = "现场下单技师信息管理")
@RestController
@RequestMapping(value = "/sceneOrderTechnician", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SceneOrderTechnicianController {


    @Autowired
    private SceneOrderTechnicianService sceneOrderTechnicianService;

    @PostMapping(value = "/addSceneTechnicianInfo")
    @ApiOperation(value = "新增现场订单技师内容", nickname = "addSceneTechnicianInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "现场下单技师信息管理", operType = OperEnum.ADD, operDesc = "新增现场订单技师内容")
    public ResultRes<String> addSceneTechnicianInfo(@RequestBody @Valid AddSceneTechnicianInfoReq req){
        return sceneOrderTechnicianService.addSceneTechnicianInfo(req);
    }


}
