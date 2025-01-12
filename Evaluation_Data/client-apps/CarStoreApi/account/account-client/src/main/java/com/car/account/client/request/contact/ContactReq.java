package com.car.account.client.request.contact;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author xielinjiang
 * @date 2021/1/14 22:53
 */
@Data
@ApiModel
public class ContactReq {

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

    /**
     * 地址
     */
    @ApiModelProperty(value="地址",name="address")
    private String address;

    /**
     * 公司
     */
    @ApiModelProperty(value="公司",name="company")
    private String company;

    /**
     * 留言
     */
    @ApiModelProperty(value="留言",name="content")
    private String content;

}
