package com.car.system.client.request.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xlj
 */
@Data
@ApiModel(value="AddRoleReq",description="新增角色请求VO")
public class AddRoleReq {

    @NotBlank(message = "请输入角色名称！")
    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

    @ApiModelProperty(value = "角色描述",name = "remarks")
    private String remarks;

    @NotEmpty(message = "请选择功能权限")
    @ApiModelProperty(value = "功能菜单权限",name = "menuList")
    public List<String> menuList;

}
