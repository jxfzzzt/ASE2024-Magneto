package com.car.system.web.model;


import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_user")
public class SysUser extends BaseModelInfo {

    /**
     * 登录名称
     */
    @Column(name = "username")
    private String username;
    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 用户姓名
     */
    @Column(name = "name")
    private String name;
    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 用户手机
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 用户状态  0：开启  1：禁用
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 角色ID
     */
    @Column(name = "role_uuid")
    private String roleUuid;

}
