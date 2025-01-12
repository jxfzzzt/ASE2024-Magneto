package com.car.utility.client.response.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下单响应返回参数")
public class CreateOrderRes {
	@ApiModelProperty(value = "租户订单编号")
	private String orderNo;

	@ApiModelProperty(value = "支付订单号")
	private String payOrderNo;

	@ApiModelProperty(value = "渠道返回body")
	private String body;

	@ApiModelProperty(value = "h5支付跳转url")
	private String mwebUrl;

	@ApiModelProperty(value = "PC native支付跳转url")
	private String codeUrl;

	@ApiModelProperty(value = "微信appkey")
	private String appKey;

	@ApiModelProperty(value = "微信prepayId")
	private String prepayId;

	@ApiModelProperty(value = "微信partnerId")
	private String partnerId;

	@ApiModelProperty(value = "微信随机字符串")
	private String nonceStr;

	@ApiModelProperty(value = "微信时间戳")
	private String timeStamp;

	@ApiModelProperty(value = "渠道返回码")
	private String channelReturnCode;

	@ApiModelProperty(value = "渠道返回信息描述")
	private String channelReturnDesc;

	@ApiModelProperty(value = "JSAPI支持二次签名参数")
	private String paySign;

}
