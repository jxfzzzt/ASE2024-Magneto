package com.car.system.client.request.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/3/14 15:48
 */
@Data
@ApiModel
public class AuthorityReq {
    @NotBlank(message = "uuid is required！")
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @NotEmpty(message = "请选择功能权限")
    @ApiModelProperty(value = "功能菜单权限",name = "menuList")
    public List<String> menuList;

}
