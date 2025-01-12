package com.car.order.client.response.maintain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 18:52
 */
@Data
@ApiModel(value="QueryMaintainRes",description="添加养护返回VO")
public class QueryMaintainRes {


    /**
     * uuid
     */
    @ApiModelProperty(value = "养护id", name = "uuid")
    private String uuid;

    /**
     * 养护标题
     */
    @ApiModelProperty(value = "养护标题", name = "maintainTitle")
    private String maintainTitle;

    /**
     * 养护封面
     */
    @ApiModelProperty(value = "养护封面", name = "maintainCover")
    private String maintainCover;

    /**
     * 养护内容
     */
    @ApiModelProperty(value = "养护内容", name = "maintainContent")
    private String maintainContent;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌uuid", name = "brandUuid")
    private String brandUuid;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌uuid", name = "brandName")
    private String brandName;


    /**
     * 车型uuid
     */
    @ApiModelProperty(value = "车型", name = "carModelUuid")
    @NotBlank(message = "请选择车型")
    private String carModelUuid;

    /**
     * 车型
     */
    @ApiModelProperty(value = "车型", name = "carModelName")
    private String carModelName;

    /**
     * 所属系统字典表attach_sys
     */
    @ApiModelProperty(value = "所属系统", name = "attachSys")
    private String attachSys;

    /**
     * 所属系统字典表attach_sys
     */
    @ApiModelProperty(value = "所属系统", name = "attachSysName")
    private String attachSysName;


    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @ApiModelProperty(value = "审核状态", name = "maintainCheckSts")
    private Integer maintainCheckSts;

    /**
     * 驳回内容
     */
    @ApiModelProperty(value = "驳回内容", name = "dtcCode")
    private String maintainRemarks;

    @ApiModelProperty(value = "创建时间", name = "createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

}