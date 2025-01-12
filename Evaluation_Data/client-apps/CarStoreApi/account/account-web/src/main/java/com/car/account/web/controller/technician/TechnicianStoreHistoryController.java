package com.car.account.web.controller.technician;


import com.car.account.client.response.storehistory.QueryStoreHistoryRes;
import com.car.account.web.service.technician.TechnicianStoreHistoryService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 技师店铺历史记录管理
 * @author xlj
 */
@Slf4j
@Api(value = "TechnicianStoreHistoryController", tags = "技师店铺历史记录管理")
@RequestMapping(value = "/storeHistory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class TechnicianStoreHistoryController {

    @Autowired
    TechnicianStoreHistoryService technicianStoreHistoryService;

    /**
     * 查询技师关联店铺历史记录信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryStoreHistory/{technicianUuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询技师关联店铺历史记录信息", nickname = "queryTechnicianCount")
    @SysOperLog(operModul = "技师店铺历史记录管理", operType = OperEnum.SELECT, operDesc = "查询技师关联店铺历史记录信息")
    public ResultRes<List<QueryStoreHistoryRes>> queryStoreHistory(@PathVariable("technicianUuid") String technicianUuid) {
        return technicianStoreHistoryService.queryStoreHistory(technicianUuid);
    }

    /**
     * 查询技师最近关联店铺
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/queryLastStoreHistory/{technicianUuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询技师最近关联店铺", nickname = "queryLastStoreHistory")
    @SysOperLog(operModul = "技师店铺历史记录管理", operType = OperEnum.SELECT, operDesc = "查询技师最近关联店铺")
    public ResultRes<QueryStoreHistoryRes> queryLastStoreHistory(@PathVariable("technicianUuid") String technicianUuid) {
        return technicianStoreHistoryService.queryLastStoreHistory(technicianUuid);
    }



}
