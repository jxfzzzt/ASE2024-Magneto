package com.car.order.client.request.content;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@ApiModel(value="CheckContentReq",description="审核内容VO对象")
@Data
public class CheckContentReq {

    @NotBlank(message = "请输入内容uuid！")
    @ApiModelProperty(value = "内容uuid",name = "uuid")
    private String uuid;

    @NotNull(message = "请输入审核状态！")
    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "checkSts")
    private Integer checkSts;
}
