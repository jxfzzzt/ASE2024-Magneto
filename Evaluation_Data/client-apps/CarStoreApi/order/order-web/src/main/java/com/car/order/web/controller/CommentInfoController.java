package com.car.order.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.comment.AddCommentReq;
import com.car.order.client.request.comment.QueryCommentInfoListReq;
import com.car.order.client.response.comment.CommentInfoListRes;
import com.car.order.web.service.comment.CommentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Slf4j
@Api(value = "CommentInfoController", tags = "评论管理")
@RequestMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class CommentInfoController {

    @Autowired
    private CommentInfoService commentInfoService;

    @PostMapping(value = "/addComment")
    @ApiOperation(value = "新增评论", nickname = "addComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.ADD, operDesc = "新增评论")
    public ResultRes<String> addComment(@RequestBody @Valid AddCommentReq req){
        return commentInfoService.addComment(req);
    }

    @PostMapping(value = "/queryCommentList")
    @ApiOperation(value = "查询评论列表信息", nickname = "queryCommentList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.SELECT, operDesc = "查询评论列表信息")
    public PageRes<List<CommentInfoListRes>> queryCommentList(@RequestBody @Valid QueryCommentInfoListReq req){
        return commentInfoService.queryCommentList(req);
    }

    /**
     * 删除评论信息
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteComment/{uuid}")
    @ApiOperation(value = "删除评论信息", nickname = "deleteComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.DELETE, operDesc = "删除评论信息")
    public ResultRes<String> deleteComment(@PathVariable(name = "uuid") String uuid){
        return commentInfoService.deleteComment(uuid);
    }

    @GetMapping(value = "/queryGoodsCommentCount/{uuid}")
    @ApiOperation(value = "查询商品评价数量", nickname = "queryGoodsCommentCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "评论管理", operType = OperEnum.SELECT, operDesc = "查询商品评价数量")
    public ResultRes<Integer> queryGoodsCommentCount(@PathVariable(name = "uuid") String uuid){
        return commentInfoService.queryGoodsCommentCount(uuid);
    }

}
