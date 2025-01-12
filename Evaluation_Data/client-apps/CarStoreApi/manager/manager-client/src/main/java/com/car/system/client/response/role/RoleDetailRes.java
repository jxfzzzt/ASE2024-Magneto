package com.car.system.client.response.role;

import com.car.common.res.BaseRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xlj
 */
@Data
@ApiModel(value="RoleDetailRes",description="查询角色详情返回VO对象")
public class RoleDetailRes extends BaseRes {
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

    @ApiModelProperty(value = "角色编码",name = "roleCode")
    private String roleCode;

    @ApiModelProperty(value = "角色状态  0：开启  1：禁用",name = "status")
    private Integer status;

    @ApiModelProperty(value = "角色描述",name = "remarks")
    private String remarks;

    @ApiModelProperty(value = "创建时间",name = "createdTime",example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "菜单权限",name = "menuList")
    private List<String> menuList;

}
