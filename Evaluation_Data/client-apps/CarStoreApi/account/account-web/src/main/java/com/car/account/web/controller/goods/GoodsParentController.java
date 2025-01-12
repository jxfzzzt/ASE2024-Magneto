package com.car.account.web.controller.goods;

import com.car.account.client.response.goods.GoodsParentRes;
import com.car.account.client.response.goods.sub.GoodsClassifyRes;
import com.car.account.web.service.goods.GoodsParentService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/23
 */
@Slf4j
@Api(value = "GoodsController", tags = "商品父组管理")
@RequestMapping(value = "/goodsParent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class GoodsParentController {

    @Autowired
    GoodsParentService goodsParentService;
    /**
     * 查询父节点所有分组，根节点传-1
     * @param parentUuid
     * @return
     */
    @GetMapping(value = "/queryListByParent/{parentUuid}")
    @ApiOperation(value = "查询父节点所有分组，根节点传-1", nickname = "queryListByParent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品父组管理", operType = OperEnum.SELECT, operDesc = "查询父节点所有分组，根节点传-1")
    public ResultRes<List<GoodsParentRes>> queryListByParent(@PathVariable(value = "parentUuid",required = false) String parentUuid) {
        return goodsParentService.queryListByParent(parentUuid);
    }

    @GetMapping(value = "/queryGoodsParent/{uuid}")
    @ApiOperation(value = "根据uuid查询商品组信息", nickname = "queryGoodsParent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品父组管理", operType = OperEnum.SELECT, operDesc = "根据uuid查询商品组信息")
    public ResultRes<GoodsParentRes> queryGoodsParent(@PathVariable(value = "uuid",required = false)  String uuid) {
        return goodsParentService.queryGoodsParent(uuid);
    }

    @GetMapping(value = "/queryList")
    @ApiOperation(value = "查询当前父节点下所有分组", nickname = "queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品父组管理", operType = OperEnum.SELECT, operDesc = "查询所有分组")
    public ResultRes<List<GoodsParentRes>> queryList() {
        return goodsParentService.queryList();
    }


    @GetMapping(value = "/queryTreeList/{parentUuid}")
    @ApiOperation(value = "查询当前父节点下所有分组树结构列表[有层级结构]", nickname = "queryTreeList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "商品父组管理", operType = OperEnum.SELECT, operDesc = "查询当前父节点下所有分组树结构列表")
    public ResultRes<List<GoodsClassifyRes>> queryTreeList(@PathVariable(value = "parentUuid",required = false) String parentUuid) {

        if(StringUtil.isBlank(parentUuid)) parentUuid = "-1";
        return goodsParentService.queryClassify(parentUuid);
    }
}
