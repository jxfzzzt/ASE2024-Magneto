package com.car.system.web.controller;


import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.enums.ResEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.user.*;
import com.car.system.client.response.user.QueryUserListRes;
import com.car.system.client.response.user.SysUserRes;
import com.car.system.client.response.user.UserDetailRes;
import com.car.system.web.model.SysUser;
import com.car.system.web.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统用户
 * @author xlj
 */
@Slf4j
@Api(value = "UserController", tags = "系统用户管理")
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @RequestMapping(value = "/queryUserByName",method = RequestMethod.GET)
    @ApiOperation(value = "通过用户名获取用户信息", nickname = "queryUserByName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.SELECT, operDesc = "通过用户名获取用户信息")
    public ResultRes<SysUserRes> queryUserByName(@RequestParam(required=true) String username){
        SysUser user = sysUserService.getUserByName(username);
        if(user == null){
            return ResultRes.error(ResEnum.NON_EXISTENT.getValue(),ResEnum.NON_EXISTENT.getDesc());
        }
        SysUserRes res = new SysUserRes();
        BeanUtils.copyProperties(user,res);
        return ResultRes.success(res);
    }

    /**
     * 查询用户列表
     * @param param
     * @return
     */
    @PostMapping(value = "/queryUserList")
    @ApiOperation(value = "查询用户列表", nickname = "queryUserList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.SELECT, operDesc = "查询用户列表")
    public PageRes<List<QueryUserListRes>> queryUserList(@RequestBody QueryUserListReq param){
        return sysUserService.queryUserList(param);
    }

    /**
     * 修改用户状态（0：开启  1：禁用）
     * @return
     */
    @PutMapping(value = "/updateUserStatus")
    @ApiOperation(value = "修改用户状态 停用/启用", nickname = "updateUserStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.UPDATE, operDesc = "修改用户状态 停用/启用")
    public ResultRes updateUserStatus(@RequestBody UpdateUserStatusReq param){
        return sysUserService.updateUserStatus(param);
    }


    /**
     * 删除用户信息
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteUser/{uuid}")
    @ApiOperation(value = "删除用户信息", nickname = "deleteUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.DELETE, operDesc = "删除用户信息")
    public ResultRes deleteUser(@PathVariable(name = "uuid")String uuid){
        return  sysUserService.deleteUser(uuid);
    }

    /**
     * 删除用户信息
     * @param uuid
     * @return
     */
    @PutMapping(value = "/resetPwd/{uuid}")
    @ApiOperation(value = "重置用户密码", nickname = "resetPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.UPDATE, operDesc = "重置用户密码")
    public ResultRes resetPwd(@PathVariable(name = "uuid")String uuid){
        return  sysUserService.resetPwd(uuid);
    }

    /**
     * 查询用户详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryUserDetail/{uuid}")
    @ApiOperation(value = "查询用户详情", nickname = "queryUserDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.SELECT, operDesc = "查询用户详情")
    public ResultRes<UserDetailRes> queryUserDetail(@PathVariable(value = "uuid")String uuid){
        return  sysUserService.queryUserDetail(uuid);
    }

    /**
     * 新增用户信息
     * @param addUserReq
     * @return
     */
    @PostMapping(value = "/addUser")
    @ApiOperation(value = "新增用户", nickname = "addUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.ADD, operDesc = "新增用户")
    public ResultRes addUser(@RequestBody AddUserReq addUserReq){
        return sysUserService.addUser(addUserReq);
    }

    /**
     *修改用户信息
     * @param updateUserReq
     * @return
     */
    @PutMapping(value = "/updateUser")
    @ApiOperation(value = "修改用户信息", nickname = "updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.UPDATE, operDesc = "修改用户信息")
    public ResultRes updateUser(@RequestBody @Valid UpdateUserReq updateUserReq){
        return sysUserService.updateUser(updateUserReq);
    }

    /**
     * 修改用户密码
     * @param updateUserPwdReq
     * @return
     */
    @PutMapping(value = "/updateUserPwd")
    @ApiOperation(value = "修改用户密码", nickname = "updateUserPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统用户管理", operType = OperEnum.UPDATE, operDesc = "修改用户密码")
    public ResultRes updateUserPwd(@RequestBody UpdateUserPwdReq updateUserPwdReq){
        return sysUserService.updateUserPwd(updateUserPwdReq);
    }
}
