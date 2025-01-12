package com.car.system.client.response.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xlj
 */
@Data
@ApiModel
public class MenuListRes {

    @ApiModelProperty(value = "系统管理",name = "sysMenuTreeList")
    public List<MenuTreeRes> sysMenuTreeList;



}
