package cn.qs.utils.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.qs.utils.HttpUtils;

public class WeixinInterfaceUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeixinInterfaceUtils.class);

	/**
	 * 获取ACCESS_TOKEN
	 */
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

	/**
	 * 获取JSAPI_TICKET
	 */
	public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	// 用于管理token
	/**
	 * 获取到的accessToken
	 */
	private static String accessToken;

	/**
	 * 最后一次获取Access_Token的时间
	 */
	private static Date lastGetAccessTokenTime;

	public static String getAccessToken() {
		if (StringUtils.isBlank(accessToken) || isExpiredAccessToken()) {
			accessToken = null;
			lastGetAccessTokenTime = null;

			Map<String, String> param = new HashMap<>();
			param.put("grant_type", "client_credential");
			param.put("appid", WeixinConstants.APPID);
			param.put("secret", WeixinConstants.APP_SECRET);

			String responseStr = HttpUtils.doGetWithParams(ACCESS_TOKEN_URL, param);
			if (StringUtils.isNotBlank(responseStr)) {
				JSONObject parseObject = JSONObject.parseObject(responseStr);
				if (parseObject != null && parseObject.containsKey("access_token")) {
					accessToken = parseObject.getString("access_token");
					lastGetAccessTokenTime = new Date();
					LOGGER.debug("调用接口获取accessToken，获取到的信息为: {}", parseObject.toString());
				}
			}
		} else {
			LOGGER.debug("使用未过时的accessToken: {}", accessToken);
		}

		return accessToken;
	}

	private static boolean isExpiredAccessToken() {
		if (lastGetAccessTokenTime == null) {
			return true;
		}

		// 1.5小时以后的就算失效
		long existTime = 5400000L;
		long now = System.currentTimeMillis();
		if (now - lastGetAccessTokenTime.getTime() > existTime) {
			return true;
		}

		return false;
	}

	/**
	 * 获取到的jsapiTicket
	 */
	private static String jsapiTicket;

	/**
	 * 最后一次获取JsapiTicket的时间
	 */
	private static Date lastGetJsapiTicketTime;

	public static String getJsapiTicket() {
		if (StringUtils.isBlank(jsapiTicket) || isExpiredJsapiTicket()) {
			jsapiTicket = null;
			lastGetJsapiTicketTime = null;

			String tmpUrl = JSAPI_TICKET_URL.replaceAll("ACCESS_TOKEN", getAccessToken());
			String responseStr = HttpUtils.doGet(tmpUrl);
			if (StringUtils.isNotBlank(responseStr)) {
				JSONObject parseObject = JSONObject.parseObject(responseStr);
				if (parseObject != null && parseObject.containsKey("ticket")) {
					jsapiTicket = parseObject.getString("ticket");
					lastGetJsapiTicketTime = new Date();
					LOGGER.debug("调用接口获取jsapiTicket，获取到的信息为: {}", parseObject.toString());
				}
			}
		} else {
			LOGGER.debug("使用未过时的jsapiTicket: {}", jsapiTicket);
		}

		return jsapiTicket;
	}

	private static boolean isExpiredJsapiTicket() {
		if (lastGetJsapiTicketTime == null) {
			return true;
		}

		// 1.5小时以后的就算失效
		long existTime = 5400000L;
		long now = System.currentTimeMillis();
		if (now - lastGetJsapiTicketTime.getTime() > existTime) {
			return true;
		}

		return false;
	}
}
