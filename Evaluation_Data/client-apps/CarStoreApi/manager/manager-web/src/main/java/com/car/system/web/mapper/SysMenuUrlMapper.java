package com.car.system.web.mapper;

import com.car.system.web.model.SysMenuUrl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface SysMenuUrlMapper extends Mapper<SysMenuUrl> {


    List<SysMenuUrl> getMenuUrlByRoleId(@Param("roleUuid") String roleUuid);

    /**
     * 查询所有的菜单路径信息
     * @return
     */
    List<SysMenuUrl> queryMenuUrl();
}
