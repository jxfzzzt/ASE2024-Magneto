package com.car.order.client.request.pay;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangwiejie
 * @since 2019-04-24
 */
@Data
@ApiModel
public class CreateOrderReq {

	@ApiModelProperty(value = "租户订单号")
	private String orderNo;

	@ApiModelProperty(value = "订单金额")
	private BigDecimal payAmount;

	@ApiModelProperty(value = "订单描述")
	private String orderDesc;

	@ApiModelProperty(value = "商户下单时间")
	private String orderTime;

	@ApiModelProperty(value = "场景信息")
	private JSONObject sceneInfo;

	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	@ApiModelProperty(value = "商品描述")
	private String goodsDesc;

	@ApiModelProperty(value = "终端IP")
	private String clientIp;

	@ApiModelProperty(value = "支付渠道  微信 ：weixin   支付宝：alipay")
	private String channelType;

	@ApiModelProperty(value = "支付方式  JSAPI(小程序/微信), NATIVE(PC扫码支付), MWEB(H5浏览器)")
	private String paymentType;

	@ApiModelProperty(value = "扩展参数")
	private String extendParams;

	@ApiModelProperty(value = "微信OPENID")
	private String openId;

}
