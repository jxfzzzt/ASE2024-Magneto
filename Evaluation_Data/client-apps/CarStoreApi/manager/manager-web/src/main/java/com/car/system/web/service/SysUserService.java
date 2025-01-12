package com.car.system.web.service;


import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.user.*;
import com.car.system.client.response.user.QueryUserListRes;
import com.car.system.client.response.user.UserDetailRes;
import com.car.system.web.model.SysUser;

import java.util.List;

public interface SysUserService {
    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    SysUser getUserByName(String username);

    /**
     * 查询用户列表
     * @param param
     * @return
     */
    PageRes<List<QueryUserListRes>> queryUserList(QueryUserListReq param);

    /**
     * 根据ID查询用户信息
     * @param userId
     * @return
     */
    SysUser getUserById(String userId);

    /**
     * 修改用户状态 停用/启用
     * @param param
     * @return
     */
    ResultRes updateUserStatus(UpdateUserStatusReq param);

    /**
     * 删除用户信息
     * @param uuid
     * @return
     */
    ResultRes deleteUser(String uuid);

    /**
     * 查询用户详情
     * @param uuid
     * @return
     */
    ResultRes<UserDetailRes> queryUserDetail(String uuid);

    /**
     * 添加用户
     * @param addUserReq
     * @return
     */
    ResultRes addUser(AddUserReq addUserReq);

    /**
     * 修改用户信息
     * @param updateUserReq
     * @return
     */
    ResultRes updateUser(UpdateUserReq updateUserReq);

    /**
     * 修改用户密码
     * @param updateUserPwdReq
     * @return
     */
    ResultRes updateUserPwd(UpdateUserPwdReq updateUserPwdReq);

    /**
     * 重置用户密码
     * @param uuid
     * @return
     */
    ResultRes resetPwd(String uuid);
}
