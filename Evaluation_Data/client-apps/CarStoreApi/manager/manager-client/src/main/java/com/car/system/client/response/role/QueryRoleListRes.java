package com.car.system.client.response.role;

import com.car.common.res.BaseRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xlj
 */
@Data
@ApiModel(value="QueryRoleListRes",description="查询角色列表返回VO对象")
public class QueryRoleListRes extends BaseRes {

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




}
