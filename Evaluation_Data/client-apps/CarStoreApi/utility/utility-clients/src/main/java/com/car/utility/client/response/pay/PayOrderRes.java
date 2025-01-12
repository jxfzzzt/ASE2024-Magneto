package com.car.utility.client.response.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class PayOrderRes {

	@ApiModelProperty(value = "商户号")
	private String stationUuid;

	@ApiModelProperty(value = "商户订单编号")
	private String tenantOrderNo;

	@ApiModelProperty(value = "订单支付日期")
	private String tenantOrderDate;

	@ApiModelProperty(value = "订单支付时间")
	private String tenantOrderTime;

	@ApiModelProperty(value = "支付渠道")
	private String channelType;

	@ApiModelProperty(value = "支付方式")
	private String paymentType;

	@ApiModelProperty(value = "订单状态 0:待支付 1:支付中；2：支付成功；3：支付失败")
	private Integer orderStatus;

	@ApiModelProperty(value = "货币类型")
	private String currency;

	@ApiModelProperty(value = "支付金额")
	private BigDecimal payAmount;

	@ApiModelProperty(value = "账户到账金额")
	private BigDecimal accountAmount;

	@ApiModelProperty(value = "手续费")
	private BigDecimal feeAmount;

	@ApiModelProperty(value = "渠道返回订单编号")
	private String channelOrderNo;

	@ApiModelProperty(value = "平台支付日期yyyyMMdd")
	private String payOrderDate;

	@ApiModelProperty(value = "平台支付时间hhmmss")
	private String payOrderTime;

	@ApiModelProperty(value = "平台支付返回码")
	private String payOrderCode;

	@ApiModelProperty(value = "平台支付返回描述")
	private String payOrderDesc;

	@ApiModelProperty(value = "渠道支付日期yyyyMMdd")
	private String channelOrderDate;

	@ApiModelProperty(value = "渠道支付日期hhmmss")
	private String channelOrderTime;

	@ApiModelProperty(value = "渠道支付订单码")
	private String channelOrderCode;

	@ApiModelProperty(value = "渠道支付订单描述")
	private String channelOrderDesc;

	@ApiModelProperty(value = "渠道返回参数")
	private String body;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(value = "是否通知")
	private Integer payIsNotify;

	@ApiModelProperty(value = "支付查询次数")
	private Integer payQueryCount;

	@ApiModelProperty(value = "支付通知次数")
	private Integer payNotifyCount;

	@ApiModelProperty(value = "渠道付款账号")
	private String channelPayAccount;

	@ApiModelProperty(value = "渠道付款户名")
	private String channelPayName;

	@ApiModelProperty(value = "付款银行")
	private String bankCode;

	@ApiModelProperty(value = "对账状态  0-未对账 1-对账成功 2-对账失败(挂账)")
	private Integer clearState;
}
