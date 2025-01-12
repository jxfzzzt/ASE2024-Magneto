package cn.qs.utils.weixin.pay;

import java.util.LinkedHashMap;
import java.util.Map;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;

import cn.qs.utils.weixin.WeixinConstants;
import cn.qs.utils.weixin.auth.WeixinJSAPISignUtils;

public class WeixinPayUtils {

	private static final WxPayConfig wxPayConfig = new WxPayConfig();

	static {
		// 公众号支付，设置公众号Id
		wxPayConfig.setAppId(WeixinConstants.APPID);
		wxPayConfig.setMchId(WeixinConstants.MCHID);
		wxPayConfig.setMchKey(WeixinConstants.API_KEY);
		wxPayConfig.setNotifyUrl(WeixinConstants.PAY_SUCCESS_NOTIFY_URL);
	}

	/**
	 * 统一下单
	 * 
	 * @return
	 */
	public static Map<String, String> unifiedOrder(String orderId, String orderName, double amount, String openId) {
		// 支付类, 所有方法都在这个类里
		BestPayServiceImpl bestPayService = new BestPayServiceImpl();
		bestPayService.setWxPayConfig(wxPayConfig);

		PayRequest payRequest = new PayRequest();
		payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
		payRequest.setOrderId(orderId);
		payRequest.setOrderName(orderName);
		payRequest.setOrderAmount(amount);
		payRequest.setOpenid(openId);
		PayResponse pay = bestPayService.pay(payRequest);
		Map<String, String> result = new LinkedHashMap<>();
		result.put("appId", pay.getAppId());
		result.put("nonceStr", pay.getNonceStr());
		result.put("timeStamp", WeixinJSAPISignUtils.getTimestamp());
		result.put("package", pay.getPackAge());
		result.put("signType", pay.getSignType());
		result.put("paySign", pay.getPaySign());

		return result;
	}

}
