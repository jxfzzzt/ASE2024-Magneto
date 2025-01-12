package com.car.utility.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "pay_order")
public class PayOrder extends BaseModelInfo {

	@ApiModelProperty(value = "商户号")
	@Column(name = "station_uuid")
	private String stationUuid;

	@ApiModelProperty(value = "商户订单编号")
	@Column(name = "tenant_order_no")
	private String tenantOrderNo;

	@ApiModelProperty(value = "订单支付日期")
	@Column(name = "tenant_order_date")
	private String tenantOrderDate;

	@ApiModelProperty(value = "订单支付时间")
	@Column(name = "tenant_order_time")
	private String tenantOrderTime;

	@ApiModelProperty(value = "支付渠道")
	@Column(name = "channel_type")
	private String channelType;

	@ApiModelProperty(value = "支付方式")
	@Column(name = "payment_type")
	private String paymentType;

	@ApiModelProperty(value = "订单状态 0:待支付 1:支付中；2：支付成功；3：支付失败")
	@Column(name = "order_status")
	private Integer orderStatus;

	@ApiModelProperty(value = "货币类型")
	@Column(name = "currency")
	private String currency;

	@ApiModelProperty(value = "支付金额")
	@Column(name = "pay_amount")
	private BigDecimal payAmount;

	@ApiModelProperty(value = "账户到账金额")
	@Column(name = "account_amount")
	private BigDecimal accountAmount;

	@ApiModelProperty(value = "手续费")
	@Column(name = "fee_amount")
	private BigDecimal feeAmount;

	@ApiModelProperty(value = "渠道返回订单编号")
	@Column(name = "channel_order_no")
	private String channelOrderNo;

	@ApiModelProperty(value = "平台支付日期yyyyMMdd")
	@Column(name = "pay_order_date")
	private String payOrderDate;

	@ApiModelProperty(value = "平台支付时间hhmmss")
	@Column(name = "pay_order_time")
	private String payOrderTime;

	@ApiModelProperty(value = "平台支付返回码")
	@Column(name = "pay_order_code")
	private String payOrderCode;

	@ApiModelProperty(value = "平台支付返回描述")
	@Column(name = "pay_order_desc")
	private String payOrderDesc;

	@ApiModelProperty(value = "渠道支付日期yyyyMMdd")
	@Column(name = "channel_order_date")
	private String channelOrderDate;

	@ApiModelProperty(value = "渠道支付日期hhmmss")
	@Column(name = "channel_order_time")
	private String channelOrderTime;

	@ApiModelProperty(value = "渠道支付订单码")
	@Column(name = "channel_order_code")
	private String channelOrderCode;

	@ApiModelProperty(value = "渠道支付订单描述")
	@Column(name = "channel_order_desc")
	private String channelOrderDesc;

	@ApiModelProperty(value = "渠道返回参数")
	@Column(name = "body")
	private String body;

	@ApiModelProperty(value = "商品名称")
	@Column(name = "goods_name")
	private String goodsName;

	@ApiModelProperty(value = "是否通知")
	@Column(name = "pay_is_notify")
	private Integer payIsNotify;

	@ApiModelProperty(value = "支付查询次数")
	@Column(name = "pay_query_count")
	private Integer payQueryCount;

	@ApiModelProperty(value = "支付通知次数")
	@Column(name = "pay_notify_count")
	private Integer payNotifyCount;

	@ApiModelProperty(value = "渠道付款账号")
	@Column(name = "channel_pay_account")
	private String channelPayAccount;

	@ApiModelProperty(value = "渠道付款户名")
	@Column(name = "channel_pay_name")
	private String channelPayName;

	@ApiModelProperty(value = "付款银行")
	@Column(name = "bank_code")
	private String bankCode;

	@ApiModelProperty(value = "对账状态  0-未对账 1-对账成功 2-对账失败(挂账)")
	@Column(name = "clear_state")
	private Integer clearState;

	@ApiModelProperty(value = "退款流水号")
	@Column(name = "refund_serial")
	private String refundSerial;

	@ApiModelProperty(value = "退款返回编")
	@Column(name = "refund_code")
	private String refundCode;

	@ApiModelProperty(value = "退款描述")
	@Column(name = "refund_desc")
	private String refundDesc;
}
