package com.car.order.client.response.dtc;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
@ApiModel(value = "QueryDtcListRes", description = "查询dtc故障列表，返回VO")
public class QueryDtcListRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "dtc故障代码", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "dtc类型", name = "dtcType")
    private String dtcType;

    @ApiModelProperty(value = "dtc类型名称", name = "dtcTypeName")
    private String dtcTypeName;

    @ApiModelProperty(value = "dtc标题", name = "dtcDefinition")
    private String dtcDefinition;

    @ApiModelProperty(value = "dtc发布关联品牌(对应车辆品牌uuid)", name = "dtcBrandUuid")
    private String dtcBrandUuid;

    @ApiModelProperty(value = "dtc购买金额", name = "dtcAmount")
    private BigDecimal dtcAmount;

    @ApiModelProperty(value = "创建时间", name = "createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "创建人", name = "createdBy")
    private String createdBy;

    @ApiModelProperty(value = "品牌名称", name = "configName")
    private String configName;

    @ApiModelProperty(value = "审核状态", name = "dtc_check_sts")
    private String dtcCheckSts;
}
