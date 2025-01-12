package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Description:用户角色
 */
@Data
@Table(name = "sys_user_role")
public class SysUserRole extends BaseModelInfo {
    /**
     * 用户id
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 角色id
     */
    @Column(name = "role_uuid")
    private String roleUuid;

}
