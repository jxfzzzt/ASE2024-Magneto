package com.car.utility.web.common.constants;

/**
 * @author xlj
 */
public class WechatPayConstants {
	public enum TradeType {
		JSAPI, NATIVE, MWEB, APP
	}

	public enum SignType {
		MD5, HMACSHA256
	}

	public enum TradeStateValue {
		SUCCESS, REFUND, NOTPAY, CLOSED, REVOKED, USERPAYING, PAYERROR
	}

	/**
	 * 异步通知失败通知微信的字符串
	 */
	public static final String FAIL_BACK_STRING = "<xml><return code><![CDATA[FAIL]]></return code></xml>";
	// 成功
	public static final String SUCCESS = "SUCCESS";
	/**
	 * 签名不匹配通知微信的字符串
	 */
	public static final String SIGN_NOT_MATCH_BACK_STRING = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名不匹配]]></return_msg></xml>";
	/**
	 * 商户号不匹配通知微信的字符串
	 */
	public static final String PARTNER_NOT_MATCH_BACK_STRING = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[商户号不匹配]]></return_msg></xml>";
	/**
	 * 错误代码
	 */
	public static final String ERR_CODE = "err_code";
	/**
	 * 异步通知微信成功字符串
	 */
	public static final String SUCCESS_BACK_STRING = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	/**
	 * 接口版本号
	 */
	public static final String VERSION = "1.0";
	/**
	 * MD5
	 */
	public static final String MD5 = "MD5";
	/**
	 * UTF-8
	 */
	public static final String UTF8 = "UTF-8";
	/**
	 * 处理中
	 */
	public static final String PROCESSING = "PROCESSING";
	/**
	 * 账单不存在
	 * */
	public static final String RETURN_CODE_NO_BILL_EXIST = "20002";
	/**
	 *
	 */
	public static final String FAIL = "fail";
}
