package com.car.system.web.model;


import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_role_menu_rel")
public class SysRoleMenuRel  extends BaseModelInfo {

    /**
     * 角色ID
     */
    @Column(name = "role_uuid")
    private String roleUuid;
    /**
     * 菜单ID
     */
    @Column(name = "menu_uuid")
    private String menuUuid;

}
