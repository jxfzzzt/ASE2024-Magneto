package com.car.order.web.controller.technician;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.response.order.consult.ConsultBaseRes;
import com.car.order.client.response.order.consult.ConsultRes;
import com.car.order.web.service.consult.ConsultService;
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
@Api(value = "AnswerController", tags = "技师问答")
@RequestMapping(value = "/answer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class AnswerController {


    @Autowired
    private ConsultService consultService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/answerList", method = RequestMethod.POST)
    @ApiOperation(value = "可被咨询的问答列表", nickname = "answer_list")
    @SysOperLog(operModul = "技师问答", operType = OperEnum.SELECT, operDesc = "查询可被咨询的问答列表")
    public PageRes<List<ConsultBaseRes>> answerList(@RequestBody PageReq param) {
        return consultService.queryConsultList(param);
    }

    public ResultRes<ConsultRes> answerDetail(){

        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @RequestMapping(value = "/preAnswerList", method = RequestMethod.POST)
    @ApiOperation(value = "技师待问答列表", nickname = "pre_answer_List")
    @SysOperLog(operModul = "技师问答", operType = OperEnum.SELECT, operDesc = "技师待问答列表")
    public PageRes<List<ConsultRes>> preAnswerList(@RequestBody PageReq param) {
        return consultService.queryPreConsultList(param);
    }
}
