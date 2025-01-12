package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.goods.QueryOrderGoodsFrontListReq;
import com.car.order.client.request.order.score.AddScoreInfoReq;
import com.car.order.client.request.order.score.QueryScoreListReq;
import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import com.car.order.client.response.order.score.QueryScoreInfoListRes;
import com.car.order.client.response.order.score.QueryScoreInfoRes;
import com.car.order.client.response.technician.CaseForTechnicianItemRes;
import com.car.order.client.response.technician.TechnicianCaseRes;
import com.car.order.web.service.score.ScoreInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Slf4j
@Api(value = "ScoreInfoController", tags = "评分管理")
@RequestMapping(value = "/score", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ScoreInfoController {


    @Autowired
    private ScoreInfoService scoreInfoService;

    @PostMapping(value = "/addScore")
    @ApiOperation(value = "新增评分信息", nickname = "addScore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评分管理", operType = OperEnum.ADD, operDesc = "新增评分信息")
    public ResultRes<String> addScore(@RequestBody @Valid AddScoreInfoReq req){
        return scoreInfoService.addScore(req);
    }


    @PostMapping(value = "/queryScoreList")
    @ApiOperation(value = "查询评分信息列表", nickname = "queryScoreList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评分管理", operType = OperEnum.SELECT, operDesc = "查询评分信息列表")
    public PageRes<List<QueryScoreInfoListRes>> queryScoreList(@RequestBody @Valid QueryScoreListReq req){
        return scoreInfoService.queryScoreList(req);
    }

    @GetMapping(value = "/queryScoreInfo/{uuid}")
    @ApiOperation(value = "查询评分详情", nickname = "queryScoreInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评分管理", operType = OperEnum.SELECT, operDesc = "查询评分详情")
    public ResultRes<QueryScoreInfoRes> queryScoreInfo(@PathVariable(name = "uuid") String uuid){
        return scoreInfoService.queryScoreInfo(uuid);
    }


    @DeleteMapping(value = "/deleteScore/{uuid}")
    @ApiOperation(value = "删除评分信息", nickname = "deleteComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.DELETE, operDesc = "删除评分信息")
    public ResultRes<String> deleteScore(@PathVariable(name = "uuid") String uuid){
        return scoreInfoService.deleteScore(uuid);
    }

    @GetMapping(value = "/queryGoodsScore/{uuid}")
    @ApiOperation(value = "查询商品/店铺/服务评分", nickname = "queryGoodsScore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.SELECT, operDesc = "查询商品/店铺/服务评分")
    public ResultRes<String> queryGoodsScore(@PathVariable(name = "uuid") String uuid){
        return scoreInfoService.queryGoodsScore(uuid);
    }


}
