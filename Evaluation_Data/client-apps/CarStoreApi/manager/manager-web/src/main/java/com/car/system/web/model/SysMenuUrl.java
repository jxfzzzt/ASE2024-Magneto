package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_menu_url")
public class SysMenuUrl extends BaseModelInfo {

    /**
     * 菜单ID
     */
    @Column(name = "menu_uuid")
    private String menuUuid;

    /**
     * 请求路径
     */
    @Column(name = "request_url")
    private String requestUrl;

    /**
     * 权限编码
     */
    @Column(name = "authority_code")
    private String authorityCode;


}
