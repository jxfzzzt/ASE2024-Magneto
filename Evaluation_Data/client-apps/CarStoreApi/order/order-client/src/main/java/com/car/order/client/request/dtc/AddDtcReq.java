package com.car.order.client.request.dtc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
@ApiModel(value="AddDtcReq",description="DTC新增故障请求VO")
public class AddDtcReq {

    @ApiModelProperty(value = "dtc故障代码", name = "dtcCode")
    @NotBlank(message = "请输入dtc故障代码")
    private String dtcCode;

    @ApiModelProperty(value = "dtc标题", name = "dtcDefinition")
    @NotBlank(message = "请输入dtc标题")
    private String dtcDefinition;

    @ApiModelProperty(value = "dtc发布关联品牌(对应车辆品牌uuid)", name = "dtcBrandUuid")
    @NotBlank(message = "请输入dtc发布关联品牌(对应车辆品牌uuid)")
    private String dtcBrandUuid;

    @ApiModelProperty(value = "dtc类型，对应字典", name = "dtcType")
    private String dtcType;

    @ApiModelProperty(value = "dtc故障说明(富文本图文)", name = "dtcExplain")
    private String dtcExplain;

    @ApiModelProperty(value = "dtc故障可能原因(富文本图文)", name = "dtcReasons")
    private String dtcReasons;

    @ApiModelProperty(value = "dtc故障诊断辅助(富文本图文)", name = "dtcDiagnose")
    private String dtcDiagnose;

    @ApiModelProperty(value = "dtc购买金额", name = "dtcAmount")
    private String dtcAmount;

    @NotNull(message = "请输入审核状态！")
    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "dtcCheckSts")
    private Integer dtcCheckSts;

    @ApiModelProperty(value = "驳回详情")
    private String dtcRemarks;
}
