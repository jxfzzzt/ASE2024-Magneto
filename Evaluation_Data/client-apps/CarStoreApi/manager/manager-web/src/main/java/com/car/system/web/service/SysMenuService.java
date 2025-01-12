package com.car.system.web.service;


import com.car.common.res.ResultRes;
import com.car.system.client.response.menu.MenuListRes;
import com.car.system.client.response.menu.MenuTreeRes;

import java.util.List;

public interface SysMenuService {

    /**
     * 查询用户系统菜单
     * @param
     * @return
     */
    ResultRes<List<MenuTreeRes>> queryMenuListByUser();

    /**
     * 获取所有系统菜单
     * @return
     */
    ResultRes<MenuListRes> queryAllMenuList();
}
