package com.car.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统操作日志表
 * @author xlj
 */
@Data
@ApiModel
public class SysOperationLogReq {

    @ApiModelProperty(value = "操作模块")
    private String operModul;

    @ApiModelProperty(value = "操作类型")
    private String operType;

    @ApiModelProperty(value = "操作描述")
    private String operDesc;

    @ApiModelProperty(value = "请求方法")
    private String operMethod;

    @ApiModelProperty(value = "请求参数")
    private String operRequParam;

    @ApiModelProperty(value = "返回参数")
    private String operRespParam;

    @ApiModelProperty(value = "请求IP")
    private String operIp;

    @ApiModelProperty(value = "请求URI")
    private String operUri;

    @ApiModelProperty(value = "创建人")
    private String createdBy;


}
