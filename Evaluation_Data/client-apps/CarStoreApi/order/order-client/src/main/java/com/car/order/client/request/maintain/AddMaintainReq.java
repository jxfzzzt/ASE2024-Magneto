package com.car.order.client.request.maintain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 18:52
 */
@Data
@ApiModel(value="AddMaintainReq",description="添加养护请求VO")
public class AddMaintainReq  {


    /**
     * 养护标题
     */
    @ApiModelProperty(value = "养护标题", name = "maintainTitle")
    @NotBlank(message = "请输入养护标题")
    private String maintainTitle;

    /**
     * 养护封面
     */
    @ApiModelProperty(value = "养护封面", name = "maintainCover")
    @NotBlank(message = "请输入养护封面")
    private String maintainCover;

    /**
     * 养护内容
     */
    @ApiModelProperty(value = "养护内容", name = "maintainContent")
    @NotBlank(message = "请输入养护内容")
    private String maintainContent;


    /**
     * 品牌uuid
     */
    @ApiModelProperty(value = "品牌uuid", name = "brandUuid")
    @NotBlank(message = "请选择品牌")
    private String brandUuid;

    /**
     * 车型uuid
     */
    @ApiModelProperty(value = "车型", name = "carModelUuid")
    @NotBlank(message = "请选择车型")
    private String carModelUuid;

    /**
     * 所属系统字典表attach_sys
     */
    @ApiModelProperty(value = "所属系统", name = "dtcCode")
    @NotBlank(message = "请选择所属系统")
    private String attachSys;


    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @ApiModelProperty(value = "审核状态", name = "maintainCheckSts")
    @NotNull(message = "请输入审核状态！")
    private Integer maintainCheckSts;

    /**
     * 驳回内容
     */
    @ApiModelProperty(value = "驳回内容", name = "dtcCode")
    private String maintainRemarks;

}