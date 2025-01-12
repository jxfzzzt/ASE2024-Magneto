package com.car.account.web.controller.store;

import com.car.account.client.request.store.*;
import com.car.account.client.response.store.QueryAreaStoreListRes;
import com.car.account.client.response.store.QueryStoreListRes;
import com.car.account.client.response.store.StoreTechnicianRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateListRes;
import com.car.account.web.mapper.store.StoreTechnicianRelateMapper;
import com.car.account.web.service.store.StoreTechnicianRelateService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Slf4j
@Api(value = "StoreTechnicianRelateController", tags = "店铺关联技师管理")
@RequestMapping(value = "/storeTechnician", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class StoreTechnicianRelateController {

    @Autowired
    StoreTechnicianRelateService storeTechnicianRelateService;

    /**
     * 新增技师关联店铺 无需token 注册时用
     * @param addStoreTechnicianRelateReq
     * @return
     */
    @PostMapping(value = "/addStoreTechnicianRelate")
    @ApiOperation(value = "新增技师关联店铺  无需token 注册时用", nickname = "addStoreTechnicianRelate")
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.ADD, operDesc = "新增技师关联店铺 无需token 注册时用")
    public ResultRes<Boolean> addTechnicianToStore(@RequestBody @Valid AddStoreTechnicianRelateReq addStoreTechnicianRelateReq){
        return storeTechnicianRelateService.addTechnicianToStore(addStoreTechnicianRelateReq);
    }

    /**
     * 新增技师关联店铺 需要token 登录后用
     * @param addRelateStoreReq
     * @return
     */
    @PostMapping(value = "/addTechnicianRelateStore")
    @ApiOperation(value = "新增技师关联店铺 需要token 登录后用", nickname = "addTechnicianRelateStore")
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.ADD, operDesc = "新增技师关联店铺 需要token 登录后用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    public ResultRes<Boolean> addTechnicianRelateStore(@RequestBody @Valid AddRelateStoreReq addRelateStoreReq){
        AddStoreTechnicianRelateReq addStoreTechnicianRelateReq = new AddStoreTechnicianRelateReq();
        addStoreTechnicianRelateReq.setTechnicianUuid(TokenHelper.getUserUuid());
        addStoreTechnicianRelateReq.setStoreUuid(addRelateStoreReq.getStoreUuid());
        return storeTechnicianRelateService.addTechnicianToStore(addStoreTechnicianRelateReq);
    }

    /**
     * 店铺解绑技师
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/storeUnbindTechnician/{uuid}")
    @ApiOperation(value = "店铺解绑技师", nickname = "storeUnbindTechnician")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.DELETE, operDesc = "店铺解绑技师")
    public ResultRes<Boolean> storeUnbindTechnician(@PathVariable(name = "uuid") String uuid){
        return storeTechnicianRelateService.storeUnbindTechnician(uuid);
    }


    /**
     * 店铺审核技师关联申请
     * @param checkStoreTechnicianRelateReq
     * @return
     */
    @PutMapping(value = "/checkStoreTechnicianRelate")
    @ApiOperation(value = "店铺审核技师关联申请", nickname = "checkStoreTechnicianRelate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.UPDATE, operDesc = "修改店铺")
    public ResultRes<Boolean> checkStoreTechnicianRelate(@RequestBody @Valid CheckStoreTechnicianRelateReq checkStoreTechnicianRelateReq){
        return storeTechnicianRelateService.checkStoreTechnicianRelate(checkStoreTechnicianRelateReq);
    }


    /**
     * 查询店铺技师关联列表
     * @param queryStoreTechnicianRelateListReq
     * @return
     */
    @PostMapping(value = "/queryStoreTechnicianRelateList")
    @ApiOperation(value = "查询店铺技师关联列表", nickname = "queryStoreTechnicianRelateList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.SELECT, operDesc = "查询店铺技师关联列表")
    public PageRes<List<StoreTechnicianRelateListRes>> queryStoreTechnicianRelateList(@RequestBody @Valid QueryStoreTechnicianRelateListReq queryStoreTechnicianRelateListReq){
        return storeTechnicianRelateService.queryStoreTechnicianRelateList(queryStoreTechnicianRelateListReq);
    }

    /**
     * 查询技师关联店铺详情
     * @return
     */
    @GetMapping(value = "/queryStoreTechnicianRelateDetail")
    @ApiOperation(value = "查询技师关联店铺详情", nickname = "queryStoreTechnicianRelateDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.SELECT, operDesc = "查询技师关联店铺详情")
    public ResultRes<StoreTechnicianRelateDetailRes> queryStoreTechnicianRelateDetail(){
        return storeTechnicianRelateService.queryStoreTechnicianRelateDetail();
    }

    /**
     * 根据区域查询店铺列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryStoreListByArea")
    @ApiOperation(value = "根据区域查询店铺列表", nickname = "queryStoreListByArea")
    @SysOperLog(operModul = "店铺关联技师管理", operType = OperEnum.SELECT, operDesc = "根据区域查询店铺列表")
    public PageRes<List<QueryAreaStoreListRes>> queryStoreListByArea(@RequestBody @Valid QueryAreaStoreListReq param){
        return storeTechnicianRelateService.queryStoreListByArea(param);
    }
}
