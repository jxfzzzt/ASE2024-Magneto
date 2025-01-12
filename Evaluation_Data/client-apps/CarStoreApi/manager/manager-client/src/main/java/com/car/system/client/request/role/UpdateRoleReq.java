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
@ApiModel(value="UpdateRoleReq",description="修改角色请求VO")
public class UpdateRoleReq {

    @NotBlank(message = "uuid is required！")
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @NotBlank(message = "请输入角色名称！")
    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

    @ApiModelProperty(value = "角色描述",name = "remarks")
    private String remarks;

    @NotEmpty(message = "请选择功能权限")
    @ApiModelProperty(value = "功能菜单权限",name = "menuList")
    public List<String> menuList;

    @ApiModelProperty(value = "数据权限",name = "dataList")
    public List<String> dataList;

}
