package com.car.system.web.model.dto;

import com.car.system.web.model.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */


@Data
public class SysMenuDto extends SysMenu {

    @ApiModelProperty(value = "父节点集合",name = "parentList")
    private String parentList;


}
