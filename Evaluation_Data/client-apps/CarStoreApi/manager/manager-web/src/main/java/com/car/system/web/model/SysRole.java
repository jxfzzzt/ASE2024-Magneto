package com.car.system.web.model;


import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_role")
public class SysRole extends BaseModelInfo {
    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;
    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private String roleCode;
    /**
     * 角色状态  0：开启  1：禁用
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

}
