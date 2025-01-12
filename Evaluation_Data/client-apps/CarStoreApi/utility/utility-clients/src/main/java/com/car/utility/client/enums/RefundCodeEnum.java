package com.car.utility.client.enums;



public enum RefundCodeEnum {
	ERROR_CODE_10001("10001","原支付订单不存在"),
	ERROR_CODE_10002("10002","原支付订单支付未成功"),
	ERROR_CODE_10003("10003","该笔订单已退款成功"),
	ERROR_CODE_10004("10004","该笔订单已存在,请勿重新提交"),
	ERROR_CODE_10005("10005","微信渠道退款申请失败"),
	ERROR_CODE_10006("10006","退款失败"),
	ERROR_CODE_10007("10007","渠道验签接口异常"),
	ERROR_CODE_10008("10008","退款中"),
	ERROR_CODE_10009("10009","渠道下单接口请求异常"),
	ERROR_CODE_10010("10010","交易待支付"),
	ERROR_CODE_10011("10011","交易成功"),
	ERROR_CODE_10012("10012","交易失败"),
	ERROR_CODE_10013("10013","交易结果未知"),
	ERROR_CODE_10014("10014","交易已关闭"),
	ERROR_CODE_10015("10015","退款成功"),
	ERROR_CODE_10016("10016","未查询到信息"),
	ERROR_CODE_10017("10017","币种类型不能为空"),
	ERROR_CODE_10018("10018","支付订单号不能为空"),
	ERROR_CODE_10019("10019","退款订单号不能为空"),
	ERROR_CODE_10020("10020","金额请求参数错误"),
	ERROR_CODE_10021("10021","请求参数无效"),
	ERROR_CODE_10022("10022","请求参数长度错误"),
	ERROR_CODE_10023("10023","租户回调地址不能为空"),
	ERROR_CODE_10024("10024","该订单已支付成功");
	private String code;
	private String desc;
	/**
	 * 构造函数
	 */
	private RefundCodeEnum(String code, String desc) {
		this.code=code;
		this.desc=desc;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
