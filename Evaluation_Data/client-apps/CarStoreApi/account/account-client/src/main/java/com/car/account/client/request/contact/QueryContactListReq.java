package com.car.account.client.request.contact;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2021/3/11 0:52
 */
@Data
@ApiModel
public class QueryContactListReq extends PageReq {
    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名",name="name")
    private String name;

    /**
     * 手机
     */
    @ApiModelProperty(value="手机",name="phone")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱",name="email")
    private String email;
}
