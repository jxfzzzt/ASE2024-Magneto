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
@ApiModel(value = "QueryDtcInfoRes", description = "查询dtc故障详情，返回VO")
public class QueryDtcInfoRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "发布人uuid", name = "dtcIssuerUuid")
    private String dtcIssuerUuid;

    @ApiModelProperty(value = "dtc故障代码", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "dtc类型，对应字典", name = "dtcType")
    private String dtcType;

    @ApiModelProperty(value = "dtc类型名称", name = "dtcTypeName")
    private String dtcTypeName;

    @ApiModelProperty(value = "dtc标题", name = "dtcDefinition")
    private String dtcDefinition;

    @ApiModelProperty(value = "dtc发布关联品牌(对应车辆品牌uuid)", name = "dtcBrandUuid")
    private String dtcBrandUuid;

    @ApiModelProperty(value = "dtc购买金额", name = "dtcAmount")
    private BigDecimal dtcAmount;

    @ApiModelProperty(value = "内容详情uuid", name = "dtcContentUuid")
    private String dtcContentUuid;

    @ApiModelProperty(value = "dtc故障说明(富文本图文)", name = "dtcExplain")
    private String dtcExplain;

    @ApiModelProperty(value = "dtc故障可能原因(富文本图文)", name = "dtcReasons")
    private String dtcReasons;

    @ApiModelProperty(value = "dtc故障诊断辅助(富文本图文)", name = "dtcDiagnose")
    private String dtcDiagnose;

    @ApiModelProperty(value = "创建时间", name = "createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "创建人", name = "createdBy")
    private String createdBy;

    @ApiModelProperty(value = "品牌名称", name = "configName")
    private String configName;

    @ApiModelProperty(value = "是否是本人发布信息，true是，false否", name = "oneself")
    private Boolean isOneself;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败", name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "订单状态uuid", name = "orderSts")
    private String orderUuid;

    @ApiModelProperty(value = "审核状态", name = "dtc_check_sts")
    private String dtcCheckSts;

    @ApiModelProperty(value = "审核原因", name = "dtc_remarks")
    private String dtcRemarks;
}
