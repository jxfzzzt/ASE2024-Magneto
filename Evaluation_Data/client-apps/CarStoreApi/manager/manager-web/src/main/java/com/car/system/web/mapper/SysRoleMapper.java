package com.car.system.web.mapper;


import com.car.system.client.request.role.QueryRoleListReq;
import com.car.system.web.model.dto.SysRoleDto;
import com.car.system.web.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysRoleMapper extends Mapper<SysRole> {

    /**
     * 查询角色列表
     * @param param
     * @return
     */
    List<SysRoleDto> queryRoleList(QueryRoleListReq param);

    /**
     * 查询角色用户梳理
     * @return
     */
    Integer queryUserRoleCount(@Param("roleUuid") String roleUuid);

    /**
     * 查询是否已存在相同角色名称
     * @param roleName
     * @param uuid
     * @return
     */
    SysRole queryRoleByRoleName(@Param("roleName") String roleName, @Param("uuid") String uuid);

    /**
     * 查询重复数量的角色信息
     * @param menuCount
     * @param
     * @return
     */
    List<SysRole> queryRepeatNumRole(@Param("menuCount") Integer menuCount);
}
