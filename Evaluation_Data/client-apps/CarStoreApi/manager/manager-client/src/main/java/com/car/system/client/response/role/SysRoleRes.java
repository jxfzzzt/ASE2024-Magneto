package com.car.system.client.response.role;

import com.car.common.res.BaseRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xlj
 */
@Data
@ApiModel(value="SysRoleRes",description="角色返回VO对象")
public class SysRoleRes extends BaseRes {

    @ApiModelProperty(value = "主键ID",name = "id")
    private Integer id;

    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

    @ApiModelProperty(value = "角色编码",name = "roleCode")
    private String roleCode;

    @ApiModelProperty(value = "角色状态  0：开启  1：禁用",name = "status")
    private String status;

    @ApiModelProperty(value = "备注",name = "remarks")
    private String remarks;

    @ApiModelProperty(value = "编辑状态  0：可编辑  1：不可编辑",name = "editStatus")
    private String editStatus;
}
