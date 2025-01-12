package com.car.system.client.response.menu;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xlj
 */
@Data
@ApiModel(value="SysMenuUrlRes",description="菜单返回VO对象")
public class SysMenuUrlRes {

    @ApiModelProperty(value = "主键ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "菜单ID",name = "menuUuid")
    private String menuUuid;

    @ApiModelProperty(value = "请求鉴权地址",name = "requestUrl")
    private String requestUrl;

    @ApiModelProperty(value = "权限编码",name = "authorityCode")
    private String authorityCode;

    @ApiModelProperty(value = "状态 0有效1无效",name = "sts")
    private String sts;

    @ApiModelProperty(value = "创建时间",name = "createTime" ,example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
