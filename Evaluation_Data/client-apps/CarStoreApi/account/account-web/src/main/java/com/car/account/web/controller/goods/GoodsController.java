package com.car.account.web.controller.goods;

import com.car.account.client.request.goods.*;
import com.car.account.client.response.goods.CalGoodsRes;
import com.car.account.client.response.goods.GoodsRes;
import com.car.account.web.mapper.store.StoreUserMapper;
import com.car.account.web.model.store.StoreUser;
import com.car.account.web.service.goods.GoodsService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Slf4j
@Api(value = "GoodsController", tags = "商品管理")
@RequestMapping(value = "/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class GoodsController {

    @Autowired
    GoodsService goodsService;
    @Resource
    StoreUserMapper storeUserMapper;

    /**
     * 新增商品
     * @param addGoodsReq
     * @return
     */
    @PostMapping(value = "/addGoods")
    @ApiOperation(value = "新增商品", nickname = "addGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.ADD, operDesc = "新增商品")
    public ResultRes<String> addGoods(@RequestBody @Valid AddGoodsReq addGoodsReq){
        return goodsService.addGoods(addGoodsReq);
    }

    /**
     * 删除商品
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteGoods/{uuid}")
    @ApiOperation(value = "删除商品", nickname = "deleteGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.DELETE, operDesc = "删除商品")
    public ResultRes<String> deleteGoods(@PathVariable(name = "uuid") String uuid){
        return goodsService.deleteGoods(uuid);
    }

    /**
     * 修改商品
     * @param updateGoodsReq
     * @return
     */
    @PutMapping(value = "/updateGoods")
    @ApiOperation(value = "修改商品", nickname = "updateGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.UPDATE, operDesc = "修改商品")
    public ResultRes<GoodsRes> updateGoods(@RequestBody @Valid UpdateGoodsReq updateGoodsReq){
        return goodsService.updateGoods(updateGoodsReq);
    }

    /**
     * 查询商品列表
     * @param queryGoodsListReq
     * @return
     */
    @PostMapping(value = "/queryGoodsList")
    @ApiOperation(value = "查询商品列表", nickname = "queryGoodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.SELECT, operDesc = "查询商品列表")
    public PageRes<List<GoodsRes>> queryGoodsList(@RequestBody @Valid QueryGoodsListReq queryGoodsListReq){
        return goodsService.queryGoodsList(queryGoodsListReq);
    }


    @PostMapping(value = "/queryStoreGoodsList")
    @ApiOperation(value = "查询商户的商品列表", nickname = "queryStoreGoodsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.SELECT, operDesc = "查询商户商品列表")
    public PageRes<List<GoodsRes>> queryStoreGoodsList(@RequestBody @Valid QueryStoreGoodsListReq queryGoodsListReq){
        Integer sellSts = queryGoodsListReq.getSellSts();

        String userUuid = TokenHelper.getUserUuid();
        Integer userType = TokenHelper.getUserType();

        QueryGoodsListReq params = new QueryGoodsListReq();
        BeanUtils.copyProperties(queryGoodsListReq,params);
        params.setSellSts(sellSts);
        if(UserTypeEnum.store.getType().equals(userType)){
            //查询店铺uuid
            log.info("用户类型店铺>>>userType:{}",userType);
            StoreUser s = new StoreUser();
            s.setSts(StsEnum.ACTIVE.getValue());
            s.setUuid(userUuid);
            StoreUser storeUser = storeUserMapper.selectOne(s);
            if(null == storeUser){
                log.error("未定位到用户关联的店铺信息>>>userUuid:{}",userUuid);
                throw new BusinessException(ResEnum.STORE_CONTACT_NOT_EXIST);
            }
            params.setStoreUuid(storeUser.getStoreUuid());

        }else if(Arrays.asList(UserTypeEnum.vehicle.getType(),UserTypeEnum.technician.getType()).contains(userType)){

            log.info("技师或车主没有调用查询商户商品列表权限");
            return PageRes.success(Collections.emptyList(), 0, 0, 0);
        }
        return queryGoodsList(params);
    }

    /**
     * 查询商品详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryGoodsDetail/{uuid}")
    @ApiOperation(value = "查询商品详情", nickname = "queryGoodsDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.SELECT, operDesc = "查询商品详情")
    public ResultRes<GoodsRes> queryGoodsDetail(@PathVariable(name = "uuid") String uuid){
        return goodsService.queryGoods(uuid);
    }



    @PostMapping(value = "/calGoods")
    @ApiOperation(value = "计算商品金额信息", nickname = "calGoods")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品管理", operType = OperEnum.SELECT, operDesc = "计算商品金额信息")
    public ResultRes<CalGoodsRes> calGoods(@RequestBody @Valid CalGoodsReq params){
        CalGoodsRes res = goodsService.calGoods(params);
        return ResultRes.success(res);
    }
    /**
     * 商品信息导出
     * @param exportReq
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", dataType = "String" ,paramType="header")
    })
    @SysOperLog(operDesc = "商品管理", operType = OperEnum.SELECT, operModul = "商品信息导出")
    @ApiOperation(value = "商品信息导出", nickname = "exportGoodsList")
    @RequestMapping(value = "/exportGoodsList", method = RequestMethod.POST)
    public void exportGoodsList(@RequestBody QueryGoodsListReq exportReq, HttpServletResponse response) throws IOException, IllegalAccessException {
        goodsService.exportGoodsList(exportReq,response);
    }
}
