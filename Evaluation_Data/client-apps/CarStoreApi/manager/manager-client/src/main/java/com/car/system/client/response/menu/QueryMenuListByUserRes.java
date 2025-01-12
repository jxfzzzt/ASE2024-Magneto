package com.car.system.client.response.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xlj
 */
@Data
@ApiModel(value="QueryMenuListByUserRes",description="查询用户系统菜单返回VO")
public class QueryMenuListByUserRes {

    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "菜单编码",name = "menuCode")
    private String menuCode;

    @ApiModelProperty(value = "父菜单ID 根节点默认为：-1",name = "parentId")
    private String parentId;

    @ApiModelProperty(value = "菜单icon",name = "menuIcon")
    private String menuIcon;

    @ApiModelProperty(value = "菜单名称",name = "menuName")
    private String menuName;

    @ApiModelProperty(value = "菜单终端模块 1：基础业务",name = "menuTerminal")
    private Integer menuTerminal;

    @ApiModelProperty(value = "菜单类型 1：目录  2：功能")
    private Integer menuType;

    @ApiModelProperty(value = "菜单类型 1：route  2：link",name = "menuType")
    private Integer routeType;

    @ApiModelProperty(value = "请求地址",name = "routeUrl")
    private String routeUrl;

    @ApiModelProperty(value = "菜单排序",name = "menuSort")
    private String menuSort;

    @ApiModelProperty(value = "菜单功能",name = "menuList")
    private List<QueryMenuListByUserRes> menuList;
}
