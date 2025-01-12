package com.car.account.web.controller.withdrawal;

import com.car.account.client.request.withdrawal.AddWithdrawalReq;
import com.car.account.client.request.withdrawal.QueryWithdrawalListReq;
import com.car.account.client.request.withdrawal.UpdateWithdrawalReq;
import com.car.account.client.response.withdrawal.QueryWithdrawalListRes;
import com.car.account.client.response.withdrawal.WithdrawalRes;
import com.car.account.web.service.withdrawal.WithdrawalService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


/**
 * @author zhouz
 * @date 2020/12/26
 */
@Slf4j
@Api(value = "WithdrawalController", tags = "提现管理")
@RequestMapping(value = "/withdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class WithdrawalController {

    @Autowired
    WithdrawalService withdrawalService;


    /**
     * 新增提现
     * @param addWithdrawalReq
     * @return
     */
    @PostMapping(value = "/addWithdrawal")
    @ApiOperation(value = "新增提现", nickname = "addWithdrawal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "提现管理", operType = OperEnum.ADD, operDesc = "新增提现")
    public ResultRes<String> addWithdrawal(@RequestBody @Valid AddWithdrawalReq addWithdrawalReq){
        return withdrawalService.addWithdrawal(addWithdrawalReq);
    }

    /**
     * 删除提现记录
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteWithdrawal/{uuid}")
    @ApiOperation(value = "删除提现记录", nickname = "deleteWithdrawal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "提现管理", operType = OperEnum.DELETE, operDesc = "删除提现记录")
    public ResultRes<String> deleteWithdrawal(@PathVariable(name = "uuid") String uuid){
        return withdrawalService.deleteWithdrawal(uuid);
    }

    /**
     * 审核提现
     * @param updateWithdrawalReq
     * @return
     */
    @PutMapping(value = "/checkWithdrawal")
    @ApiOperation(value = "审核提现", nickname = "checkWithdrawal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "提现管理", operType = OperEnum.UPDATE, operDesc = "审核提现")
    public ResultRes<String> checkWithdrawal(@RequestBody @Valid UpdateWithdrawalReq updateWithdrawalReq){
        return withdrawalService.checkWithdrawal(updateWithdrawalReq);
    }

    /**
     * 查询提现列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryWithdrawalList")
    @ApiOperation(value = "查询提现列表", nickname = "queryWithdrawalList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "提现管理", operType = OperEnum.SELECT, operDesc = "查询提现列表")
    public PageRes<List<QueryWithdrawalListRes>> queryGoodsList(@RequestBody @Valid QueryWithdrawalListReq param){
        return withdrawalService.queryWithdrawalList( param);
    }

    /**
     * 查询提现详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryWithdrawalDetail/{uuid}")
    @ApiOperation(value = "查询提现详情", nickname = "queryWithdrawalDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "提现管理", operType = OperEnum.SELECT, operDesc = "查询提现详情")
    public ResultRes<WithdrawalRes> queryWithdrawalDetailByUuid(@PathVariable(name = "uuid")String uuid ){
        return withdrawalService.queryWithdrawalDetailByUuid(uuid);
    }

    /**
     * 提现信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "提现管理", operType = OperEnum.SELECT, operModul = "提现信息导出")
    @ApiOperation(value = "提现信息导出", nickname = "exportWithdrawalList")
    @RequestMapping(value = "/exportWithdrawalList", method = RequestMethod.POST)
    public void exportWithdrawalList(@RequestBody QueryWithdrawalListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        withdrawalService.exportWithdrawalList(exportReq,response);
    }
}
