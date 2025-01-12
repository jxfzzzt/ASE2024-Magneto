package com.car.system.web.mapper;


import com.car.system.web.model.SysRole;
import com.car.system.web.model.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysUserRoleMapper extends Mapper<SysUserRole> {


    int deleteUserRole(@Param("userUuid") String userUuid);

    /**
     * 查询当前用户角色信息
     * @param userUuid
     * @return
     */
    List<SysRole> queryUserRoleList(String userUuid);
}
