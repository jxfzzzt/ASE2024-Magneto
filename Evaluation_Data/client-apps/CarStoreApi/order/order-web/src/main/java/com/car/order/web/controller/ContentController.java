package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.content.AddContentReq;
import com.car.order.client.request.content.CheckContentReq;
import com.car.order.client.request.content.QueryContentListReq;
import com.car.order.client.response.content.ContentDetailRes;
import com.car.order.client.response.content.QueryContentListRes;
import com.car.order.client.response.order.consult.CaseDetails;
import com.car.order.web.service.content.ContentService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Slf4j
@Api(value = "ContentController", tags = "内容管理")
@RequestMapping(value = "/store", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ContentController {


    @Autowired
    private ContentService contentService;

    /**
     * 新增内容
     * @param addContentReq
     * @return
     */
    @PostMapping(value = "/addContent")
    @ApiOperation(value = "新增内容", nickname = "addContent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "内容管理", operType = OperEnum.ADD, operDesc = "新增内容")
    public ResultRes<String> addStore(@RequestBody @Valid AddContentReq addContentReq){
        return contentService.addContent(addContentReq);
    }

    /**
     * 删除内容
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteContent/{uuid}")
    @ApiOperation(value = "删除内容", nickname = "deleteContent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "内容管理", operType = OperEnum.DELETE, operDesc = "删除内容")
    public ResultRes<String> deleteContent(@PathVariable(name = "uuid") String uuid){
        return contentService.deleteContent(uuid);
    }

    /**
     * 审核内容
     * @param checkContentReq
     * @return
     */
    @PutMapping(value = "/checkContent")
    @ApiOperation(value = "审核内容", nickname = "checkContent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "内容管理", operType = OperEnum.UPDATE, operDesc = "审核内容")
    public ResultRes<String> checkContent(@RequestBody @Valid CheckContentReq checkContentReq){
        return contentService.checkContent(checkContentReq);
    }

    /**
     * 查询内容列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryContentList")
    @ApiOperation(value = "查询内容列表", nickname = "queryContentList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "内容管理", operType = OperEnum.SELECT, operDesc = "查询内容列表")
    public PageRes<List<QueryContentListRes>> queryContentList(@RequestBody @Valid QueryContentListReq param){
        return contentService.queryContentList(param);
    }

    /**
     * 查询内容详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryContentDetail/{uuid}")
    @ApiOperation(value = "查询内容详情", nickname = "queryContentDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "内容管理", operType = OperEnum.SELECT, operDesc = "查询内容详情")
    public ResultRes<ContentDetailRes> queryContentDetail(@PathVariable(name = "uuid") String uuid){
        return contentService.queryContentDetail(uuid);
    }


    /**
     * 案例详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/getCaseDetails/{uuid}")
    @ApiOperation(value = "案例详情", nickname = "queryContentDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "案例详情", operType = OperEnum.SELECT, operDesc = "案例详情")
    public ResultRes<CaseDetails> getCaseDetails(@PathVariable(name = "uuid") String uuid){
        return contentService.getCaseDetails(uuid);
    }


    /**
     * 我发布的案例
     * @param uuid
     * @return
     */
    @GetMapping(value = "/getMyCaseList/{uuid}")
    @ApiOperation(value = "我发布的案例", nickname = "queryContentDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "我发布的案例", operType = OperEnum.SELECT, operDesc = "案例详情")
    public ResultRes<List<CaseDetails>> getMyCaseList(@ApiParam("用户ID") @PathVariable(name = "uuid") String uuid,
                                                      @ApiParam("我发布的1  我购买的2") @PathVariable(name = "type")Integer type){
        return contentService.getMyCaseList(uuid,type);
    }

    /**
     * 我购买的案例
     * @param uuid
     * @return
    @GetMapping(value = "/purchaseCase/{uuid}")
    @ApiOperation(value = "我购买的案例", nickname = "queryContentDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "我购买的案例", operType = OperEnum.SELECT, operDesc = "案例详情")
    public ResultRes<List<CaseDetails>> purchaseCase(@ApiParam("用户ID") @PathVariable(name = "uuid") String uuid){
        return contentService.purchaseCase(uuid);
    }*/
}
