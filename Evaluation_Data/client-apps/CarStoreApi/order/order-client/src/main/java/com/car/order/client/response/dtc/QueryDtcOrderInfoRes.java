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
 * Date:  2021/2/19
 */

@Data
@ApiModel(value = "QueryDtcOrderInfoRes", description = "查询dtc故障订单详情，返回VO")
public class QueryDtcOrderInfoRes {

    @ApiModelProperty(value = "主键ID", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "dtc故障uuid", name = "dtcUuid")
    private String dtcUuid;

    @ApiModelProperty(value = "订单编号", name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "订单金额", name = "orderAmount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付", name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败", name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "查看次数,最多可看3次,超过3次重新购买", name = "readCount")
    private Integer readCount;

    @ApiModelProperty(value = "购买者uuid", name = "buyerUuid")
    private String buyerUuid;

    @ApiModelProperty(value = "购买者姓名", name = "buyerName")
    private String buyerName;

    @ApiModelProperty(value = "购买者手机号码", name = "buyerMobile")
    private String buyerMobile;

    @ApiModelProperty(value = "发布者uuid", name = "issuerUuid")
    private String issuerUuid;

    @ApiModelProperty(value = "发布者姓名", name = "issuerName")
    private String issuerName;

    @ApiModelProperty(value = "发布者手机号码", name = "issuerMobile")
    private String issuerMobile;

    @ApiModelProperty(value = "发布人类型：0：后台发布，1：技师 ，2：店铺", name = "dtcIssuerType")
    private Integer dtcIssuerType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "dtc故障代码", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "dtc发布关联品牌(对应车辆品牌uuid)", name = "dtcBrandUuid")
    private String dtcBrandUuid;

    @ApiModelProperty(value = "dtc定义(标题)", name = "dtcDefinition")
    private String dtcDefinition;

    @ApiModelProperty(value = "dtc故障说明(富文本图文)", name = "dtcExplain")
    private String dtcExplain;

    @ApiModelProperty(value = "dtc故障可能原因(富文本图文)", name = "dtcReasons")
    private String dtcReasons;

    @ApiModelProperty(value = "dtc故障诊断辅助(富文本图文)", name = "dtcDiagnose")
    private String dtcDiagnose;

    @ApiModelProperty(value = "是否支付，true已支付，false未支付", name = "isPay")
    private Boolean isPay;

    @ApiModelProperty(value = "DTC类型", name = "dtcType")
    private String dtcType;
}
