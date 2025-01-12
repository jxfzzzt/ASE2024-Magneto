package com.xiaoleilu.ucloud.core;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.HttpUtil;
import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.log.LogWrapper;
import com.xiaoleilu.ucloud.core.Response.RetCode;
import com.xiaoleilu.ucloud.util.Config;
import com.xiaoleilu.ucloud.util.Global;

/**
 * Ucloud Api请求客户端
 * @author Looly
 *
 */
public class UcloudApiClient {
	private final static LogWrapper log = Log.get();
	
	/** 公共参数设置 */
	private Config config;
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param config 公共参数设置，包括公钥、私钥、API URL等
	 */
	public UcloudApiClient(Config config) {
		super();
		this.config = config;
	}
	
	/**
	 * 构造，使用默认的公共参数配置文件
	 */
	public UcloudApiClient() {
		super();
		this.config = Config.createFromSetting();
	}
	// --------------------------------------------------------------- Constructor end
	
	/**
	 * 获得配置文件
	 * @return 配置文件
	 */
	public Config getConfig() {
		return this.config;
	}
	
	/**
	 * get请求API
	 * @param resource 请求的资源
	 * @param param 参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public String getForStr(String resource, Param param){
		final String uri = StrUtil.format("{}{}?{}", config.getBaseUrl(), resource, param.genHttpParam(config));
		log.debug("Get: {}", uri);
		
//		HttpResponse response = HttpRequestUtil.prepareGet(resource).send();
//		
//		final int statusCode = response.statusCode();
//		if(statusCode != 200) {
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("RetCode"	, RetCode.ERROR);
//			jsonObject.put("Message", "Status Code is" + statusCode);
//			return jsonObject.toString();
//		}
//		return response.bodyText();
		
		String resStr = null;
		try {
			resStr = HttpUtil.get(uri, Global.CHARSET, false);
		} catch (IOException e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("RetCode"	, RetCode.ERROR);
			jsonObject.put("Message", e.getMessage());
			resStr = jsonObject.toString();
		}
		return resStr;
	}
	
	/**
	 * get请求API
	 * @param resource 请求的资源
	 * @param param 参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public StandardResponse get(String resource, Param param){
		if(param == null) {
			param = Param.create();
		}
		
		return StandardResponse.parse(getForStr(resource, param));
	}
	
	/**
	 * get请求API<br>
	 * resource 使用默认的 /
	 * @param param 参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public StandardResponse get(Param param){
		return get("/", param);
	}
	
	/**
	 * get请求API<br>
	 * resource 使用默认的 /
	 * @param action API指令
	 * @param param 参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public StandardResponse get(Action action, Param param){
		if(param == null) {
			param = Param.create();
		}
		
		param.setAction(action);
		return get("/", param);
	}
}
