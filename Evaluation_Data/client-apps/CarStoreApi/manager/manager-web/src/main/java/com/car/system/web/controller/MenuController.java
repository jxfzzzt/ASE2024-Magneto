package com.car.system.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.system.client.response.menu.MenuListRes;
import com.car.system.client.response.menu.MenuTreeRes;
import com.car.system.web.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理
 */
@Slf4j
@Api(value = "MenuController", tags = "系统菜单管理")
@RequestMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;


    /**
     * 根据用户查询所属菜单信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/queryMenuListByUser", method = RequestMethod.GET)
    @ApiOperation(value = "查询用户系统菜单", nickname = "queryMenuListByUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "系统菜单管理", operType = OperEnum.SELECT, operDesc = "查询用户系统菜单")
    public ResultRes<List<MenuTreeRes>> queryMenuListByUser() {
        return sysMenuService.queryMenuListByUser();
    }

    /**
     * 获取所有系统菜单
     *
     * @param
     * @return
     * @throws
     */
    @RequestMapping(value = "/queryAllMenuList", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有系统菜单", nickname = "queryAllMenuList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
    @SysOperLog(operModul = "系统菜单管理", operType = OperEnum.SELECT, operDesc = "获取所有系统菜单")
    public ResultRes<MenuListRes> queryAllMenuList() {
        return sysMenuService.queryAllMenuList();
    }


}
