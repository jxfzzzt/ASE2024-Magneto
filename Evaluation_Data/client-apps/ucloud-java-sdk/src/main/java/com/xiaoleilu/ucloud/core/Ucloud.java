package com.xiaoleilu.ucloud.core;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.log.LogWrapper;
import com.xiaoleilu.ucloud.util.Config;

/**
 * UCloud模块公共基础类，各模块继承此类用于调用UcloudApiClient
 * 
 * @author Looly
 *
 */
public class Ucloud {
	protected final static LogWrapper log = Log.get();

	// --------------------------------------------------------------- Static method start
	/**
	 * 创建构建器，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public static UcloudBuilder builder(){
		return new UcloudBuilder();
	}
	/**
	 * 创建构建器
	 * 
	 * @param config 配置文件
	 */
	public static UcloudBuilder builder(Config config) {
		return new UcloudBuilder(config);
	}

	/**
	 * 创建构建器
	 * 
	 * @param client UcloudApiClient
	 */
	public  static UcloudBuilder builder(UcloudApiClient client) {
		return new UcloudBuilder(client);
	}
	// --------------------------------------------------------------- Static method end

	/** Ucloud客户端 */
	protected UcloudApiClient client;
	/** 参数，用于使用构建模式时使用 */
	protected Param param;

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public Ucloud() {
		this.client = new UcloudApiClient();
	}

	/**
	 * 构造
	 * 
	 * @param config 配置文件
	 */
	public Ucloud(Config config) {
		this.client = new UcloudApiClient(config);
	}

	/**
	 * 构造
	 * 
	 * @param client UcloudApiClient
	 */
	public Ucloud(UcloudApiClient client) {
		this.client = client;
	}

	// --------------------------------------------------------------- Constructor end

	/**
	 * @return 客户端对象
	 */
	public UcloudApiClient getClient() {
		return this.client;
	}

	/**
	 * 发送请求
	 * 
	 * @param param 参数
	 * @return 响应结果
	 */
	public Response send(Param param) {
		return this.client.get(param);
	}
	
	/**
	 * 发送请求
	 * 
	 * @return 响应结果
	 */
	public Response send() {
		return send(this.param);
	}
}
