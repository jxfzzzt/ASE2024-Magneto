package com.car.utility.web.common.utils.weixin;

import com.car.utility.client.request.pay.CreateOrderReq;
import com.car.utility.web.model.PayOrder;
import lombok.Data;

import java.util.Map;

@Data
public class ChannelContext {
	private CreateOrderReq createOrderParam;
	private PayOrder payOrder;
	private Map<String, String> map;


}
