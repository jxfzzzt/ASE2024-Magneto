package com.car.system.web.service;


import com.car.system.web.model.SysMenuUrl;

import java.util.List;

public interface SysMenuUrlService {

    List<SysMenuUrl> getMenuUrlByRoleId(String roleUuid);

    /**
     * 查询所有的菜单路径信息
     * @return
     */
    List<SysMenuUrl> queryMenuUrl();
}
