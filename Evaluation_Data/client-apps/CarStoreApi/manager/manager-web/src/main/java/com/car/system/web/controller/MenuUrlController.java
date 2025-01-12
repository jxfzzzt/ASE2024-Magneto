package com.car.system.web.controller;


import com.car.common.annotation.SysOperLog;
import com.car.common.constant.CacheNameConstant;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.RedisUtils;
import com.car.system.client.response.menu.SysMenuUrlRes;
import com.car.system.web.model.SysMenuUrl;
import com.car.system.web.service.SysMenuUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理
 */
@Slf4j
@Api(value = "MenuUrlController", tags = "系统角色路径管理")
@RequestMapping(value = "/menuUrl", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MenuUrlController {

    @Autowired
    private SysMenuUrlService sysMenuUrlService;

    @Autowired
    RedisUtils redisUtils;


    /**
     * 通过用户ID获取角色信息
     * @param roleUuid
     * @return
     */
    @RequestMapping(value = "/queryMenuUrlByRoleId/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "通过角色ID获取所有菜单路径", nickname = "queryMenuUrlByRoleId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色路径管理", operType = OperEnum.SELECT, operDesc = "通过角色ID获取所有菜单路径")
    public ResultRes<List<SysMenuUrlRes>> queryMenuUrlByRoleId(@PathVariable("uuid") String roleUuid) {
        List<SysMenuUrlRes> resList=new ArrayList<>();
        List<SysMenuUrlRes> list = redisUtils.getList(String.format(CacheNameConstant.SERVICE_ROLE_MENU,roleUuid),SysMenuUrlRes.class);
        if (!CollectionUtils.isEmpty(list)){
            resList=list;
        }else {
            List<SysMenuUrl> menuList = sysMenuUrlService.getMenuUrlByRoleId(roleUuid);
            //封装返回输出对象
            for (SysMenuUrl menuUrl :menuList) {
                SysMenuUrlRes resBean = new SysMenuUrlRes();
                BeanUtils.copyProperties(menuUrl,resBean);
                resList.add(resBean);
            }
            redisUtils.lPush(String.format(CacheNameConstant.SERVICE_ROLE_MENU,roleUuid),resList,60*5L);
        }

        return ResultRes.success(resList);
    }

    /**
     * 获取所有的菜单路径信息
     * @param
     * @return
     */
    @RequestMapping(value = "/queryMenuUrl",method = RequestMethod.GET)
    @ApiOperation(value = "获取所有的菜单路径信息(超管用户使用)", nickname = "queryMenuUrl")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色路径管理", operType = OperEnum.SELECT, operDesc = "获取所有的菜单路径信息(超管用户使用)")
    public ResultRes<List<SysMenuUrlRes>> queryMenuUrl() {
        List<SysMenuUrl> menuList = sysMenuUrlService.queryMenuUrl();
        //封装返回输出对象
        List<SysMenuUrlRes> resList = new ArrayList<SysMenuUrlRes>();
        for (SysMenuUrl menuUrl :menuList) {
            SysMenuUrlRes resBean = new SysMenuUrlRes();
            BeanUtils.copyProperties(menuUrl,resBean);
            resList.add(resBean);

        }
        return ResultRes.success(resList);
    }
}
