package com.xiaoleilu.ucloud.util;

import com.xiaoleilu.hutool.CharsetUtil;

/**
 * 全局设定类
 * @author Looly
 *
 */
public class Global {
	
	/** 全局字符集编码 */
	public final static String CHARSET = CharsetUtil.UTF_8;
	
	/** 默认的请求API基本路径 */
	public final static String DEFAULT_BASE_URL = "https://api.ucloud.cn";
	
	/** 请求API接口时使用的浏览器标识 */
	public final static String USER_AGENT = "Ucloud Java SDK by Luxiaolei";
}
