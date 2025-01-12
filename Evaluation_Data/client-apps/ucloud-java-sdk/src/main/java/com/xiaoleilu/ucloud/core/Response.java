package com.xiaoleilu.ucloud.core;

import com.alibaba.fastjson.JSONObject;

/**
 * API响应内容接口
 * @author Looly
 *
 */
public interface Response {
	
	/** 返回状态码的Name */
	static final String RET_CODE = "RetCode";
	/** API指令的Name */
	static final String ACTION = "Action";
	/** 返回消息的Name */
	static final String MESSAGE = "Message";
	/** 返回结果数的Name */
	static final String TOTAL_COUNT = "TotalCount";
	
	/**
	 * 获得对象
	 * @param key KEY
	 * @return 对象
	 */
	public Object get(String key);
	
	/**
	 * 获得响应状态码
	 * @return 响应状态码
	 */
	public int getRetCode();
	
	/**
	 * 获得API指令
	 * @return API指令
	 */
	public String getAction();
	
	/**
	 * 获得返回的消息（一般为错误消息）
	 * @return 返回消息
	 */
	public String getMessage();
	
	/**
	 * 获得满足条件结果数
	 * @return 满足条件结果数
	 */
	public int getTotalCount();
	
	/**
	 * 获得响应JSON对象
	 * @return 响应JSON对象
	 */
	public JSONObject getJson();
	
	/**
	 * @return 请求是否正常
	 */
	public boolean isOk();
	
	/**
	 * 输出格式化后的JSON字符串
	 * @return 格式化后的JSON字符串
	 */
	public String toPretty();
	
	/**
	 * API返回码
	 * @author Looly
	 *
	 */
	public static class RetCode {
		/** API请求正常 */
		public final static int OK = 0;
		
		/** API请求未知异常 */
		public final static int ERROR= -1;
		
		/** 用户不存在 */
		public final static int USER_NOT_EXISTS= 171;
		
		/** 验证签名错误 */
		public final static int SIGNATURE_VERFY_AC_ERROR= 172;
		
		/** 主机未关 */
		public final static int UHOST_NOT_SHUTDOWN= 8010;
	}
}
