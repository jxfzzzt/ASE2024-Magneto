package com.car.order.client.request.order.consult;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 22021/1/2
 */
@ApiModel(value="QueryConsultListReq",description="查询咨询列表请求VO对象")
@Data
public class QueryConsultListReq extends PageReq {


    @NotBlank(message = "请输入车主Uuid！")
    @ApiModelProperty(value = "车主Uuid (必填) consultType = 0时,旁听列表过滤自己提出的问题" +
            "consultType = 1 或 4 时查询当前用户的咨询或旁听列表",name = "carOwnerUuid")
    private String carOwnerUuid;

    @NotNull(message = "请输入咨询类型！")
    @ApiModelProperty(value = "咨询类型 (必填): 0 所有(可旁听列表) 1 咨询  4 旁听",name = "consultType")
    private Integer consultType;

}
