package com.xiaoleilu.ucloud.util;

import jodd.http.HttpRequest;

/**
 * Http工具类
 * @author Looly
 *
 */
public class HttpRequestUtil {
	
	private static final String HEADER_USER_AGENT = "User-Agent";
	
	/**
	 * 构建Get请求
	 * @param uri URL
	 * @return HttpRequest
	 */
	public static HttpRequest prepareGet(String uri){
		return commonSet(HttpRequest.get(uri));
	}
	
	/**
	 * 构建Put请求
	 * @param uri URL
	 * @return HttpRequest
	 */
	public static HttpRequest preparePut(String uri){
		return commonSet(HttpRequest.put(uri));
	}
	
	/**
	 * 构建Post请求
	 * @param uri URL
	 * @return HttpRequest
	 */
	public static HttpRequest preparePost(String uri){
		return commonSet(HttpRequest.post(uri));
	}
	
	/**
	 * 构建Delete请求
	 * @param uri URL
	 * @return HttpRequest
	 */
	public static HttpRequest prepareDelete(String uri){
		return commonSet(HttpRequest.delete(uri));
	}
	
	/**
	 * 添加请求的公共属性
	 * @param request 请求对象
	 * @return 原请求对象
	 */
	private static HttpRequest commonSet(HttpRequest request) {
		return request
			.queryEncoding(Global.CHARSET)
			.header(HEADER_USER_AGENT, Global.USER_AGENT);
	}
}
