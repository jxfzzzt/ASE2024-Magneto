package com.car.system.web.service.impl;


import com.car.common.utils.RedisUtils;
import com.car.system.web.service.SysMenuUrlService;
import com.car.system.web.model.SysMenuUrl;
import com.car.system.web.mapper.SysMenuUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuUrlServiceImpl implements SysMenuUrlService {

    @Autowired
    SysMenuUrlMapper sysMenuUrlMapper;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public List<SysMenuUrl> getMenuUrlByRoleId(String roleUuid) {
        return sysMenuUrlMapper.getMenuUrlByRoleId(roleUuid);
    }

    /**
     * 查询所有的菜单路径信息
     * @return
     */
    @Override
    public List<SysMenuUrl> queryMenuUrl() {
        return sysMenuUrlMapper.queryMenuUrl();
    }
}
