package com.car.account.web.controller.store;

import com.alibaba.fastjson.JSONArray;
import com.car.account.client.request.store.*;
import com.car.account.client.response.goods.ext.StoreGoodsClassifyRes;
import com.car.account.client.response.store.*;
import com.car.account.web.model.store.Store;
import com.car.account.web.service.store.StoreService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Slf4j
@Api(value = "StoreController", tags = "店铺管理")
@RequestMapping(value = "/store", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 新增店铺
     * @param addStoreReq
     * @return
     */
    @PostMapping(value = "/addStore")
    @ApiOperation(value = "新增店铺", nickname = "addStore")
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.ADD, operDesc = "新增店铺")
    public ResultRes<String> addStore(@RequestBody @Valid AddStoreReq addStoreReq){
        return storeService.addStore(addStoreReq);
    }

    /**
     * 删除店铺
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteStore/{uuid}")
    @ApiOperation(value = "删除店铺", nickname = "deleteStore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.DELETE, operDesc = "删除店铺")
    public ResultRes<String> deleteStore(@PathVariable(name = "uuid") String uuid){
        return storeService.deleteStore(uuid);
    }

    /**
     * 修改店铺
     * @param updateStoreReq
     * @return
     */
    @PutMapping(value = "/updateStore")
    @ApiOperation(value = "修改店铺", nickname = "updateStore")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.UPDATE, operDesc = "修改店铺")
    public ResultRes<String> updateStore(@RequestBody @Valid UpdateStoreReq updateStoreReq){
        log.info(JSONArray.toJSONString(updateStoreReq));
        return storeService.updateStore(updateStoreReq);
    }

    /**
     * 修改店铺账号信息
     * @param storeAccountReq
     * @return
     */
    @PutMapping(value = "/updateStoreAccount")
    @ApiOperation(value = "修改店铺账号信息", nickname = "updateStoreAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.UPDATE, operDesc = "修改店铺账号信息")
    public ResultRes<String> updateStoreAccount(@RequestBody @Valid StoreAccountReq storeAccountReq){
        return storeService.updateStoreAccount(storeAccountReq);
    }

    /**
     * 查询店铺列表
     * @param queryStoreListReq
     * @return
     */
    @PostMapping(value = "/queryStoreList")
    @ApiOperation(value = "查询店铺列表", nickname = "queryStoreList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺列表")
    public PageRes<List<QueryStoreListRes>> queryStoreList(@RequestBody @Valid QueryStoreListReq queryStoreListReq){
        return storeService.queryStoreList(queryStoreListReq);
    }

    /**
     * 查询共享店铺列表
     * @param
     * @return
     */
    @PostMapping(value = "/queryShareStoreList")
    @ApiOperation(value = "查询共享店铺列表", nickname = "queryShareStoreList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询共享店铺列表")
    public ResultRes<List<QueryShareStoreListRes>> queryShareStoreList(@RequestBody QueryShareStoreListReq param){
        return storeService.queryShareStoreList(param);
    }

    /**
     * 查询店铺详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryStoreDetail/{uuid}")
    @ApiOperation(value = "查询店铺详情", nickname = "queryStoreDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺详情")
    public ResultRes<StoreDetailRes> queryStoreDetail(@PathVariable(name = "uuid") String uuid){
        return storeService.queryStoreDetail(uuid);
    }

    /**
     * 根据登录用户token查询店铺详情
     * @return
     */
    @GetMapping(value = "/queryStoreDetailByUser")
    @ApiOperation(value = "根据登录用户token查询店铺详情", nickname = "queryStoreDetailByUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "根据登录用户token查询店铺详情")
    public ResultRes<StoreDetailRes> queryStoreDetailByUser(){
        Store store = storeService.getStore();
        StoreDetailRes storeDetailRes = new StoreDetailRes();
        BeanUtils.copyProperties(store,storeDetailRes);
        return ResultRes.success(storeDetailRes);
    }

    @GetMapping(value = "/queryStoreDetail")
    @ApiOperation(value = "查询店铺详情", nickname = "queryStoreDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺详情")
    public ResultRes<StoreDetailRes> queryStoreDetail(){
        return storeService.queryStoreDetail();
    }

    @GetMapping(value = "/queryAccount")
    @ApiOperation(value = "查询店铺账户信息", nickname = "queryAccount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺账户信息")
    public ResultRes<StoreAccountRes> queryAccount(){
        return storeService.queryStoreAccount();
    }

    /**
     * 查询店铺详情
     * @param storeUuid
     * @return
     */
    @GetMapping(value = "/queryStoreGoodsClassify/{storeUuid}")
    @ApiOperation(value = "查询店铺商品分类列表", nickname = "queryStoreGoodsClassify")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺商品分类列表")
    public ResultRes<List<StoreGoodsClassifyRes>> queryStoreGoodsClassifyRes(@PathVariable(name = "storeUuid") String storeUuid){
        List<StoreGoodsClassifyRes> storeGoodsClassifyRes = storeService.queryStoreGoodsClassifyRes(storeUuid);
        return ResultRes.success(storeGoodsClassifyRes);
    }

    /**
     * 店铺信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "店铺管理", operType = OperEnum.SELECT, operModul = "店铺信息导出")
    @ApiOperation(value = "店铺信息导出", nickname = "exportStoreList")
    @RequestMapping(value = "/exportStoreList", method = RequestMethod.POST)
    public void exportStoreList(@RequestBody QueryStoreListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        storeService.exportStoreList(exportReq,response);
    }

    /**
     * 查询店铺联系人详情
     * @param storeUserUuid
     * @return
     */
    @GetMapping(value = "/queryStoreUserInfo/{storeUserUuid}")
    @ApiOperation(value = "查询店铺商品分类列表", nickname = "queryStoreUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "店铺管理", operType = OperEnum.SELECT, operDesc = "查询店铺商品分类列表")
    public ResultRes<StoreUserRes> queryStoreUserInfo(@PathVariable(name = "storeUserUuid") String storeUserUuid){
        return storeService.queryStoreUserInfo(storeUserUuid);
    }
}
