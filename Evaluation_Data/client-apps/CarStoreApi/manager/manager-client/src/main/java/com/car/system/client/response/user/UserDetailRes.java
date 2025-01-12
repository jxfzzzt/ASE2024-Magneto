package com.car.system.client.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xlj
 */
@Data
@ApiModel(value="UserDetailRes",description="查询用户详情返回VO对象")
public class UserDetailRes {
    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;
    @ApiModelProperty(value = "登录名称",name = "username")
    private String username;
    @ApiModelProperty(value = "登录密码",name = "password")
    private String password;
    @ApiModelProperty(value = "用户姓名",name = "name")
    private String name;
    @ApiModelProperty(value = "用户邮箱",name = "email")
    private String email;
    @ApiModelProperty(value = "用户手机",name = "phone")
    private String phone;
    @ApiModelProperty(value = "用户状态  0：开启  1：禁用",name = "status")
    private Integer status;
    @ApiModelProperty(value = "角色ID",name = "roleUuid")
    private String roleUuid;
    @ApiModelProperty(value = "角色ID",name = "roleUuid")
    private String roleName;
    @ApiModelProperty(value = "创建人",name = "createdBy")
    private String createdBy;
    @ApiModelProperty(value = "创建时间",name = "createdTime",example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    @ApiModelProperty(value = "修改人",name = "lastUpdatedBy")
    private String lastUpdatedBy;
    @ApiModelProperty(value = "修改时间",name = "lastUpdatedTime",example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedTime;

}
