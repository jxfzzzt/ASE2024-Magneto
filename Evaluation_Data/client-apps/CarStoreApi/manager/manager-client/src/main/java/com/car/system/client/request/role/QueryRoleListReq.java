package com.car.system.client.request.role;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/3/28 11:31
 */
@Data
@ApiModel(value="QueryRoleListReq",description="查询角色列表请求VO")
public class QueryRoleListReq extends PageReq {

    @ApiModelProperty(value = "角色名称",name = "roleName")
    private String roleName;

}
