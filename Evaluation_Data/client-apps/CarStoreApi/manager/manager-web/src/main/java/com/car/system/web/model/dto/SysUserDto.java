package com.car.system.web.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: guoshihao
 * @Date: 2019/5/7 16:15
 * @Description:用户管理列表返回DTO
 */
@ApiModel(value="SysUserDto",description="用户管理列表返回DTO")
@Data
public class SysUserDto {

    @ApiModelProperty(value = "主键ID",name = "id")
    private Integer id;

    @ApiModelProperty(value = "登录名称",name = "userName")
    private String userName;

    @ApiModelProperty(value = "用户姓名",name = "name")
    private String name;

    @ApiModelProperty(value = "用户状态  0：开启  1：禁用",name = "status")
    private String status;

    @ApiModelProperty(value = "创建时间",name = "createTime",example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "//状态 0有效1无效",name = "sts")
    private String sts;
}
