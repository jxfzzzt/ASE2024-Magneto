package com.car.account.web.controller.technician;

import com.car.account.client.response.technician.caset.CaseStatisticsRes;
import com.car.account.web.service.technician.TechnicianService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 案例
 * @author zhangyp
 * @date 2021/1/29 0:39
 */
@Slf4j
@Api(value = "CaseController", tags = "技师案例")
@RequestMapping(value = "/case", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class CaseController {
    @Autowired
    private TechnicianService technicianService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/case_statistics", method = RequestMethod.GET)
    @ApiOperation(value = "技师案例汇总列表", nickname = "case_statistics")
    @SysOperLog(operModul = "技师案例", operType = OperEnum.SELECT, operDesc = "查询技师案例列表")
    public PageRes<List<CaseStatisticsRes>> caseStatistics(@RequestBody PageReq param) {
        return technicianService.queryCaseStatisticsList(param);
    }
}
