package com.car.order.client.request.dtc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author: niushuaixiang
 * Date:  2021/3/17
 */
@Data
@ApiModel(value="CheckDtcReq",description="DTC审核请求VO")
public class CheckDtcReq {

    @ApiModelProperty(value = "dtc故障uuid", name = "dtcUuid")
    @NotBlank(message = "请输入dtc故障uuid！")
    private String dtcUuid;

    @NotNull(message = "请输入审核状态！")
    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "dtcCheckSts")
    private Integer dtcCheckSts;

    @ApiModelProperty(value = "驳回详情")
    private String dtcRemarks;
}
