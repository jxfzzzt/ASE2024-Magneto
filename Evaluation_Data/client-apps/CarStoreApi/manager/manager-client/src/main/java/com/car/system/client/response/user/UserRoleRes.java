package com.car.system.client.response.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xlj
 */
@Data
@ApiModel(value="UserRoleRes",description="用户角色返回VO对象")
public class UserRoleRes {
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

    @ApiModelProperty(value = "角色编码",name = "roleCode")
    private String roleCode;

    @ApiModelProperty(value = "角色描述",name = "remarks")
    private String remarks;



}
