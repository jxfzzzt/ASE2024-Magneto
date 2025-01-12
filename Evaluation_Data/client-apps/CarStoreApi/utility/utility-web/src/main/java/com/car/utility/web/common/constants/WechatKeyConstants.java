package com.car.utility.web.common.constants;

public class WechatKeyConstants {
	public enum AppKeyValue {
		timestamp, partnerid, appid, prepayid, noncestr
	}

	public enum H5KeyValue {
		mch_id, nonce_str, app_id, prepay_id, mweb_url
	}

	/** 交易类型 **/
	public static final String TRADE_TYPE = "trade_type";
	/** 场景信息 **/
	public static final String SCENE_INFO = "scene_info";
	/** 标价币种 */
	public static final String FEE_TYPE = "fee_type";
	/** 订单金额 */
	public static final String TOTAL_FEE = "total_fee";
	/** 退款金额 **/
	public static final String REFUND_FEE = "refund_fee";
	/** 公众账号ID **/
	public static final String APPID = "appid";
	/** 支付宝加密私钥 **/
	public static final String PKCS8 = "pkcs8";
	/** 商户号 **/
	public static final String MCH_ID = "mch_id";
	/** 签名类型 **/
	public static final String SIGN_TYPE = "sign_type";
	/** 随机字符串 **/
	public static final String NONCE_STR = "nonce_str";
	/** 商户交易号 **/
	public static final String OUT_TRADE_NO = "out_trade_no";
	/** 支付平台退款单号 **/
	public static final String OUT_REFUND_NO = "out_refund_no";
	/** 商品描述 **/
	public static final String BODY = "body";
	/** 终端IP **/
	public static final String SPBILL_CREATE_IP = "spbill_create_ip";
	/** 通知地址 **/
	public static final String NOTIFY_URL = "notify_url";
	/** weixinSignKey **/
	public static final String SIGN_KEY = "signKey";
	/** HMACSHA235 **/
	public static final String HMACSHA256 = "HMAC-SHA256";
	/** md5 **/
	public static final String MD5 = "MD5";
	/** 签名 **/
	public static final String SIGN = "sign";
	/** 返回状态码 **/
	public static final String RETURN_CODE = "return_code";
	/** 业务结果 **/
	public static final String RESULT_CODE = "result_code";
	/** 返回信息 **/
	public static final String RETURN_MSG = "return_msg";
	/** 错误代码 **/
	public static final String ERR_CODE = "err_code";
	/** 错误代码 **/
	public static final String ERROR_CODE = "error_code";
	/** 错误代码描述 **/
	public static final String ERR_CODE_DES = "err_code_des";
	/** 微信商户证书 **/
	public static final String WEIXIN_CERT = "weixinCert";
	/** 微信退款单号 **/
	public static final String REFUND_ID = "refund_id";
	/** 预支付交易会话标识 **/
	public static final String PREPAY_ID = "prepay_id";
	/** H5支付跳转页面 **/
	public static final String MWEB_URL = "mweb_url";
	/** PC native支付跳转页面 **/
	public static final String CODE_URL = "code_url";
	/** 交易时间 **/
	public static final String TIME_END = "time_end";
	/** 微信支付订单号 **/
	public static final String TRANSACTION_ID = "transaction_id";
	/** 接口版本号 **/
	public static final String VERSION = "version";
	/** 交易状态 **/
	public static final String TRADE_STATE = "trade_state";
	/** 付款银行 **/
	public static final String BANK_TYPE = "bank_type";
	/** 用户标识 **/
	public static final String OPENID = "openid";
	/** 加密信息 **/
	public static final String REQ_INFO = "req_info";
	/** 退款状态 **/
	public static final String RETURN_STATUS = "refund_status";
	/** 账单日期 **/
	public static final String BILL_DATE = "bill_date";
	/** 账单类型，返回当日所有的订单信息 **/
	public static final String BILL_TYPE_ALL = "ALL";
	/** 账单类型 **/
	public static final String BILL_TYPE = "bill_type";
	/** 压缩账单 **/
	public static final String TAR_TYPE = "tar_type";
	/** 压缩账单，格式为.gzip的压缩包 **/
	public static final String TAR_TYPE_GZIP = "GZIP";
	/** 账单错误码 **/
	public static final String BILL_ERROR_CODE = "error_code";
	/** 数据包 **/
	public static final String PACKAGE = "package";
	/** 扩展字段 **/
	public static final String WX_PACKAGE = "wx_package";
	/** 时间戳 **/
	public static final String TIME_STAMP = "time_stamp";

	public static final String TIMESTAMP = "timeStamp";
	/** 退款成功时间 **/
	public static final String SUCCESS_TIME = "success_time";

}
