package com.car.system.web.service;


import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.role.AddRoleReq;
import com.car.system.client.request.role.QueryRoleListReq;
import com.car.system.client.request.role.UpdateRoleReq;
import com.car.system.client.response.role.QueryRoleListRes;
import com.car.system.client.response.role.RoleDetailRes;

import java.util.List;

public interface SysRoleService {

    /**
     * 新增角色
     * @param addRoleReq
     * @return
     */
    ResultRes addRole(AddRoleReq addRoleReq);

    /**
     * 修改角色信息
     * @param updateRoleReq
     * @return
     */
    ResultRes updateRole(UpdateRoleReq updateRoleReq);

    /**
     * 删除角色信息
     * @param uuid
     * @return
     */
    ResultRes deleteRole(String uuid);

    /**
     * 查询角色详情
     * @param uuid
     * @return
     */
    ResultRes<RoleDetailRes> queryRoleDetail(String uuid);

    /**
     * 查询角色列表
     * @param queryRoleListReq
     * @return
     */
    PageRes<List<QueryRoleListRes>> queryRoleList(QueryRoleListReq queryRoleListReq);

    /**
     * 检查角色是否存在
     * @param addRoleReq
     * @return
     */
    ResultRes checkRole(AddRoleReq addRoleReq);
}
