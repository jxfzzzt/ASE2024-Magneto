package com.car.system.web.controller;


import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.role.AddRoleReq;
import com.car.system.client.request.role.QueryRoleListReq;
import com.car.system.client.request.role.UpdateRoleReq;
import com.car.system.client.response.role.QueryRoleListRes;
import com.car.system.client.response.role.RoleDetailRes;
import com.car.system.web.service.SysRoleService;
import com.car.system.web.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理
 */
@Slf4j
@Api(value = "RoleController", tags = "系统角色管理")
@RequestMapping(value = "/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    protected SysUserService sysUserService;


    /**
     * 新增角色信息
     * @param addRoleReq
     * @return
     */
    @PostMapping(value = "/addRole")
    @ApiOperation(value = "新增角色信息", nickname = "addRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.ADD, operDesc = "新增角色信息")
    public ResultRes addRole(@RequestBody @Autowired AddRoleReq addRoleReq){
        return sysRoleService.addRole(addRoleReq);
    }

    /**
     * 检查角色是否存在
     * @param addRoleReq
     * @return
     */
    @PostMapping(value = "/checkRole")
    @ApiOperation(value = "检查角色是否存在", nickname = "checkRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.SELECT, operDesc = "检查角色是否存在")
    public ResultRes checkRole(@RequestBody @Autowired AddRoleReq addRoleReq){
        return sysRoleService.checkRole(addRoleReq);
    }


    /**
     * 修改角色信息
     * @param updateRoleReq
     * @return
     */
    @PutMapping(value = "/updateRole")
    @ApiOperation(value = "修改角色信息", nickname = "updateRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.UPDATE, operDesc = "修改角色信息")
    public ResultRes updateRole(@RequestBody @Valid UpdateRoleReq updateRoleReq){
        return sysRoleService.updateRole(updateRoleReq);
    }

    /**
     * 删除角色信息
     * @param uuid
     * @return
     */
    @DeleteMapping(value = "/deleteRole/{uuid}")
    @ApiOperation(value = "删除角色信息", nickname = "deleteRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.DELETE, operDesc = "删除角色信息")
    public ResultRes deleteRole(@PathVariable(name = "uuid") String uuid){
        return sysRoleService.deleteRole(uuid);
    }

    /**
     * 查询角色详情
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryRoleDetail/{uuid}")
    @ApiOperation(value = "查询角色详情", nickname = "queryRoleDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.SELECT, operDesc = "查询角色详情")
    public ResultRes<RoleDetailRes> queryRoleDetail(@PathVariable(value = "uuid") String uuid){
        return sysRoleService.queryRoleDetail(uuid);
    }

    /**
     * 查询角色详情
     * @param queryRoleListReq
     * @return
     */
    @PostMapping(value = "/queryRoleList")
    @ApiOperation(value = "查询角色列表", nickname = "queryRoleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String" ,paramType="header")
    })
    @SysOperLog(operModul = "系统角色管理", operType = OperEnum.SELECT, operDesc = "查询角色列表")
    public PageRes<List<QueryRoleListRes>> queryRoleList(@RequestBody QueryRoleListReq queryRoleListReq){
        return sysRoleService.queryRoleList(queryRoleListReq);
    }
}
