package com.car.system.web.model.dto;

import com.car.system.web.model.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class SysRoleDto extends SysRole {

    @ApiModelProperty(value = "组织名称",name = "organizeName")
    private String organizeName;


}
