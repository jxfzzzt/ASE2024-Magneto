package com.car.system.web.mapper;


import com.car.system.client.request.user.QueryUserListReq;
import com.car.system.web.model.dto.SysUserDto;
import com.car.system.web.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysUserMapper extends Mapper<SysUser> {

    /**
     * 查询用户列表
     * @param param
     * @return
     */
    List<SysUser> queryUserList(QueryUserListReq param);


    /**
     *
     * @Description:通过用户名获取用户信息列表（人员管理）
     *
     * @param: [userName]
     * @return: com.car.idc.system.web.common.utils.ResultRes<java.util.List<SysUserDto>>
     * @auther: guoshihao
     * @date: 2019/5/8 11:31
     */
    List<SysUserDto> qryUserListByName(@Param("userName") String userName);

    /**
     *
     * @Description:用户停用/启用
     *
     * @param: [userId]
     * @return: com.car.idc.system.web.common.utils.ResultRes
     * @auther: guoshihao
     * @date: 2019/5/8 13:50
     */
    void stopUser(@Param("userId") int userId, @Param("status") String status);

    /**
     *
     * @Description:修改用户信息
     *
     * @param: [sysUserVo]
     * @return: com.car.idc.system.web.common.utils.ResultRes
     * @auther: guoshihao
     * @date: 2019/5/8 13:54
     */
    int updateUser(@Param("passWord") String passWord, @Param("status") String status, @Param("userId") int userId);


    /**
     *
     * @Description:删除用户
     *
     * @param: [userId]
     * @return: com.car.idc.system.web.common.utils.ResultRes
     * @auther: guoshihao
     * @date: 2019/5/8 13:57
     */
    int delUserById(@Param("userId") int userId, @Param("sts") Integer sts);


}
