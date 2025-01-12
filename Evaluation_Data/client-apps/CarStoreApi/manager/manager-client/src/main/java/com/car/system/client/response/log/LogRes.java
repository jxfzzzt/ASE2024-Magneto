package com.car.system.client.response.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author
 * @date 2020/5/26 16:26
 */
@Data
@ApiModel
public class LogRes {

    @ApiModelProperty(value = "主键ID")
    private String uuid;


    @ApiModelProperty(value = "操作模块")
    private String operModul;


    @ApiModelProperty(value = "操作类型")
    private String operType;

    @ApiModelProperty(value = "操作描述")
    private String operDesc;

    @ApiModelProperty(value = "请求参数")
    private String operRequParam;

    @ApiModelProperty(value = "返回参数")
    private String operRespParam;

    @ApiModelProperty(value = "请求IP")
    private String operIp;

    @ApiModelProperty(value = "oper_uri")
    private String operUri;

    @ApiModelProperty(value = "操作方法")
    private String operMethod;

    @ApiModelProperty(value = "创建时间",name = "createdTime",example = "yyyy-MM-dd HH:mi:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdBy;


}
