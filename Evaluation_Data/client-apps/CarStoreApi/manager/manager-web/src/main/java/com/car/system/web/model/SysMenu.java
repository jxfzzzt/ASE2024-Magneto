package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_menu")
public class SysMenu extends BaseModelInfo {
    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;
    /**
     * 父菜单ID 根节点默认为：-1
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 菜单icon
     */
    @Column(name = "menu_icon")
    private String menuIcon;
    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;
    /**
     * 菜单终端模块 1：基础业务 2：智慧社区  3：系统管理  4:桌面告警程序
     */
    @Column(name = "menu_terminal")
    private Integer menuTerminal;
    /**
     * 菜单类型 1：route  2：link
     */
    @Column(name = "route_type")
    private Integer routeType;
    /**
     * 路由地址
     */
    @Column(name = "route_url")
    private String routeUrl;
    /**
     * 菜单类型 1：目录  2：功能
     */
    @Column(name = "menu_type")
    private Integer menuType;
    /**
     * 菜单排序
     */
    @Column(name = "menu_sort")
    private String menuSort;
    /**
     * 菜单状态  0：开启  1：禁用
     */
    @Column(name = "status")
    private Integer status;
}
