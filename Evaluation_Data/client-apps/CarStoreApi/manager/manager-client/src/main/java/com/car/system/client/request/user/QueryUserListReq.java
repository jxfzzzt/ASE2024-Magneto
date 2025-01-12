package com.car.system.client.request.user;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author xlj
 */
@ApiModel(value="QueryUserListReq",description="查询用户列表VO对象")
@Data
public class QueryUserListReq extends PageReq {
    @ApiModelProperty(value = "检索内容",name = "searchValue")
    private String searchValue;

    @ApiModelProperty(value = "开始时间",name = "beginTime")
    private String beginTime;

    @ApiModelProperty(value = "结束时间",name = "endTime")
    private String endTime;

    @ApiModelProperty(value = "用户状态  0：开启  1：禁用",name = "status")
    private Integer status;
}
