package com.car.gateway.service;

import com.car.common.constant.CacheNameConstant;
import com.car.common.res.ResultRes;
import com.car.common.utils.RedisUtils;
import com.car.gateway.common.enums.RoleMenuEnum;
import com.car.system.client.feign.SystemFeign;
import com.car.system.client.response.menu.SysMenuUrlRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Buerger
 * @date 2020/10/21 16:05
 */
@Service
@Slf4j
public class RoleMenuUrlService {

    @Autowired
    SystemFeign systemFeign;

    @Autowired
    RedisUtils redisUtils;



    public ResultRes<List<String>> getRoleMenuUrls(List<String> roleIds){
        List<SysMenuUrlRes> allUrls=new ArrayList<>();

        if (CollectionUtils.isEmpty(roleIds)){
            return ResultRes.success(new ArrayList<>());
        }
        for (String roleId : roleIds) {
            List<SysMenuUrlRes> sysMenuUrlRes;
            List<SysMenuUrlRes> list = redisUtils.getList(String.format(CacheNameConstant.SERVICE_ROLE_MENU,roleId), SysMenuUrlRes.class);
            if (!CollectionUtils.isEmpty(list)){
                sysMenuUrlRes=list;
            }else {
                ResultRes<List<SysMenuUrlRes>> listResultRes = systemFeign.queryMenuUrlByRoleId(roleId);
                if (!listResultRes.isSuccess()){
                    log.error("获取角色菜单列表失败");
                    return ResultRes.error(RoleMenuEnum.FAIL_TO_GET_ROLE_MENU.getValue(),RoleMenuEnum.FAIL_TO_GET_ROLE_MENU.getDesc(),new ArrayList<>());
                }
                sysMenuUrlRes=listResultRes.getData();
            }
            allUrls.addAll(sysMenuUrlRes);
        }
        return ResultRes.success(allUrls.stream().map(SysMenuUrlRes::getRequestUrl).distinct().collect(Collectors.toList()));
    }

}
