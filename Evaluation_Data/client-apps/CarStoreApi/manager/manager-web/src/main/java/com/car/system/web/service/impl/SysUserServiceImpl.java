package com.car.system.web.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.car.system.web.service.SysUserService;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StatusEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.BCryptPwdUtils;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.system.client.request.user.*;
import com.car.system.client.response.user.QueryUserListRes;
import com.car.system.client.response.user.UserDetailRes;
import com.car.system.web.common.constants.ConfigConsts;
import com.car.system.web.model.SysRole;
import com.car.system.web.model.SysUser;
import com.car.system.web.mapper.SysMenuMapper;
import com.car.system.web.mapper.SysRoleMapper;
import com.car.system.web.mapper.SysRoleMenuRelMapper;
import com.car.system.web.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    ConfigConsts configConsts;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysRoleMenuRelMapper sysRoleMenuRelMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return
     */
    @Override
    public SysUser getUserByName(String username) {
        SysUser searchUser = new SysUser();
        searchUser.setUsername(username);
        searchUser.setSts(StsEnum.ACTIVE.getValue());
        return sysUserMapper.selectOne(searchUser);
    }

    /**
     * 查询用户列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<QueryUserListRes>> queryUserList(QueryUserListReq param) {
        //获取当前用户下所有的组织权限
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SysUser> userList = sysUserMapper.queryUserList(param);
        PageInfo<SysUser> pageInfo = new PageInfo<>(userList);
        List<QueryUserListRes> userResList = new ArrayList<QueryUserListRes>();
        for (SysUser user: userList) {
            QueryUserListRes res = new QueryUserListRes();
            BeanUtils.copyProperties(user,res);
            SysRole role = sysRoleMapper.selectByPrimaryKey(user.getRoleUuid());
            if(!StringUtils.isEmpty(role)){
                res.setRoleName(role.getRoleName());
            }
            userResList.add(res);
        }
        return PageRes.success(userResList,pageInfo.getPageSize(),(int) pageInfo.getTotal(),pageInfo.getPages());
    }

    /**
     * 修改用户状态 停用/启用
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResultRes updateUserStatus(UpdateUserStatusReq param) {
        if(!StatusEnum.contains(param.getStatus())){
            log.error("修改用户状态请求参数错误，status参数不在指定范围，请求参数为：{}", JSONArray.toJSONString(param));
            throw new BusinessException(ResEnum.LACK_PARAMETER.getValue(),"status参数值错误，范围：0：开启  1：禁用");
        }
        log.debug("根据用户ID查询用户信息");
        SysUser user = sysUserMapper.selectByPrimaryKey(param.getUuid());
        if(user == null){
            log.error("通过用户ID：{}未匹配到用户信息",param.getUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        user.setStatus(param.getStatus());
        user.setLastUpdatedBy(TokenHelper.getUserName());
        user.setLastUpdatedTime(new Date());
        int resCount = sysUserMapper.updateByPrimaryKeySelective(user);
        if(resCount <= 0){
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success();
    }

    /**
     * 删除用户信息
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResultRes deleteUser(String uuid) {
        log.debug("根据用户ID查询用户信息");
        SysUser user = sysUserMapper.selectByPrimaryKey(uuid);
        if(user == null){
            log.error("通过用户ID：{}未匹配到用户信息",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        if(configConsts.defaultSupperUserUuid.equals(user.getUuid())){
            log.error("超级管理员账号不可被删除");
            throw new BusinessException(ResEnum.DELETE_SUPPER_ADMIN_ERROR.getValue(),ResEnum.DELETE_SUPPER_ADMIN_ERROR.getDesc());
        }
        user.setSts(StsEnum.INVALID.getValue());
        user.setLastUpdatedBy(TokenHelper.getUserName());
        user.setLastUpdatedTime(new Date());
        int resCount = sysUserMapper.updateByPrimaryKeySelective(user);
        if(resCount <= 0){
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success();
    }

    /**
     * 查询用户详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<UserDetailRes> queryUserDetail(String uuid) {
        log.debug("根据用户ID查询用户信息");
        SysUser user = sysUserMapper.selectByPrimaryKey(uuid);
        if(user == null){
            log.error("通过用户ID：{}未匹配到用户信息",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        UserDetailRes userDetail = new UserDetailRes();
        BeanUtils.copyProperties(user,userDetail);
        //查询当前用户角色信息
        SysRole role = sysRoleMapper.selectByPrimaryKey(user.getRoleUuid());
        if(!StringUtils.isEmpty(role)){
            userDetail.setRoleName(role.getRoleName());
        }
        return ResultRes.success(userDetail);
    }


    /**
     * 根据ID查询用户信息
     * @param userId
     * @return
     */
    @Override
    public SysUser getUserById(String userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }


    /**
     * 新增用户信息
     * @param addUserReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResultRes addUser(AddUserReq addUserReq) {
        log.debug("验证用户名是否已存在");
        SysUser searchUser =  getUserByName(addUserReq.getUsername());
        if(searchUser != null){
            throw new BusinessException(ResEnum.LOGIN_USER_EXIST.getValue(),ResEnum.LOGIN_USER_EXIST.getDesc());
        }
        //验证角色是否存在
        SysRole role = sysRoleMapper.selectByPrimaryKey(addUserReq.getRoleUuid());
        if(StringUtils.isEmpty(role) || StsEnum.INVALID.getValue().equals(role.getSts())){
            throw new BusinessException(ResEnum.LACK_PARAMETER.getValue(),ResEnum.LACK_PARAMETER.getDesc());
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(addUserReq,sysUser);
        sysUser.setSts(StsEnum.ACTIVE.getValue());
        sysUser.setCreatedBy(TokenHelper.getUserName());
        sysUser.setCreatedTime(new Date());
        log.debug("当用户未上传密码时候，采用系统默认密码：{}",configConsts.defaultUserPwd);
        if(StringUtils.isEmpty(sysUser.getPassword())){
            sysUser.setPassword(BCryptPwdUtils.getBcryptPwd(configConsts.defaultUserPwd));
        }else {
            sysUser.setPassword(BCryptPwdUtils.getBcryptPwd(sysUser.getPassword()));
        }
        sysUser.setUuid(UuidUtils.getUuid());
        log.debug(">>>>>>>>>>>>>新增用户基本信息 返回："+ JSONArray.toJSONString(sysUser));
        int resUser = sysUserMapper.insert(sysUser);
        log.debug(">>>>>>>>>>>>>新增用户基本信息 返回："+resUser);
        if(resUser <= 0){
            log.error("新增用户信息受影响行数为：{}，请求参数为：{}",resUser, JSONArray.toJSONString(sysUser));
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success(sysUser.getUuid());
    }

    /**
     * 修改用户信息
     * @param updateUserReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public ResultRes updateUser(UpdateUserReq updateUserReq) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(updateUserReq.getUuid());
        if(sysUser == null){
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        //验证角色是否存在
        SysRole role = sysRoleMapper.selectByPrimaryKey(updateUserReq.getRoleUuid());
        if(StringUtils.isEmpty(role) || StsEnum.INVALID.getValue().equals(role.getSts())){
            throw new BusinessException(ResEnum.LACK_PARAMETER.getValue(),ResEnum.LACK_PARAMETER.getDesc());
        }
        log.debug("当密码不为空时，修改当前用户密码：{}",updateUserReq.getPassword());
        if(!StringUtils.isEmpty(updateUserReq.getPassword())){
            sysUser.setPassword(BCryptPwdUtils.getBcryptPwd(updateUserReq.getPassword()));
        }
        sysUser.setName(updateUserReq.getName());
        sysUser.setEmail(updateUserReq.getEmail());
        sysUser.setPhone(updateUserReq.getPhone());
        sysUser.setStatus(updateUserReq.getStatus());
        sysUser.setLastUpdatedTime(new Date());
        sysUser.setLastUpdatedBy(TokenHelper.getUserName());
        sysUser.setRoleUuid(updateUserReq.getRoleUuid());
        int updCount = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(updCount <= 0){
            log.error("修改用户信息受影响行数为：{}，请求参数为：{}",updCount, JSONArray.toJSONString(sysUser));
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success();
    }

    /**
     * 修改用户密码
     * @param updateUserPwdReq
     * @return
     */
    @Override
    public ResultRes updateUserPwd(UpdateUserPwdReq updateUserPwdReq) {
        String userUuid = updateUserPwdReq.getUuid();
        if(StringUtils.isEmpty(updateUserPwdReq.getUuid())){
            userUuid = TokenHelper.getUserUuid();
        }
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userUuid);
        if(sysUser == null){
            log.error("通过用户ID：{}未查询到对应用户信息",userUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        //匹配验证原始密码是否正确
        if(!BCryptPwdUtils.matchesBcryptPwd(updateUserPwdReq.getOriginalPassword(),sysUser.getPassword())){
            log.error("用户密码匹配不一致，页面原始密码为：{}",updateUserPwdReq.getOriginalPassword());
            throw new BusinessException(ResEnum.ORIGINAL_PASSWORD_ERROR.getValue(),ResEnum.ORIGINAL_PASSWORD_ERROR.getDesc());
        }

        log.debug("重置当前用户密码，执行参数：{}", JSONArray.toJSONString(updateUserPwdReq));
        String newPassWord = BCryptPwdUtils.getBcryptPwd(updateUserPwdReq.getPassword());
        sysUser.setPassword(newPassWord);
        sysUser.setLastUpdatedBy(TokenHelper.getUserName());
        sysUser.setLastUpdatedTime(new Date());
        int resCount = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(resCount <= 0){
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success();
    }

    /**
     * 重置用户密码
     * @param uuid
     * @return
     */
    @Override
    public ResultRes resetPwd(String uuid) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(uuid);
        if(sysUser == null){
            log.error("通过用户ID：{}未查询到对应用户信息",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        sysUser.setPassword(BCryptPwdUtils.getBcryptPwd(configConsts.defaultUserPwd));
        sysUser.setLastUpdatedBy(TokenHelper.getUserName());
        sysUser.setLastUpdatedTime(new Date());
        int resCount = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(resCount <= 0){
            throw new BusinessException(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        return ResultRes.success();
    }
}
