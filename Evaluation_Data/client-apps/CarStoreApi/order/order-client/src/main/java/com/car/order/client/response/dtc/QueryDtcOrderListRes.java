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
@ApiModel(value = "QueryDtcOrderListRes", description = "查询dtc故障订单列表，返回VO")
public class QueryDtcOrderListRes {

    @ApiModelProperty(value = "主键ID", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "dtc故障code", name = "dtcCode")
    private String dtcCode;

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

    @ApiModelProperty(value = "发布人类型：0：后台发布，1：技师 ，2：店铺", name = "dtcIssuerType")
    private Integer dtcIssuerType;

    @ApiModelProperty(value = "下单时间 yyyy-MM-dd",name="createdTime",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdTime;

    @ApiModelProperty(value = "dtc故障说明(富文本图文)", name = "dtcExplain")
    private String dtcExplain;
}
