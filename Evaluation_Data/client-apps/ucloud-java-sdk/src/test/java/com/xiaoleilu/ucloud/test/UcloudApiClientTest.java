package com.xiaoleilu.ucloud.test;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.util.Config;

/**
 * UcloudApiClient使用样例
 * 
 * @author Looly
 *
 */
public class UcloudApiClientTest {
	private final static Logger log = Log.get();

	UcloudApiClient client;

	public UcloudApiClient createUcloudApiClient() {
		// 使用默认的Ucloud Api请求客户端
		// 默认读取classpath下的config.setting文件。文件内容请参阅doc/config_sample.setting
		return new UcloudApiClient();
	}

	public UcloudApiClient createUcloudApiClient2() {
		// 自定义配置内容
		final Config config = new Config(
		// 公钥
				"ucloudsomeone@example.com1296235120854146120",
				// 私钥
				"46f09bb9fab4f12dfc160dae12273d5332b5debe",
				// 请求API的URL
				"https://api.ucloud.cn");
		return new UcloudApiClient(config);
	}

	public void send() {
		/*
		 * 创建UcloudApiClient方法一
		 * 使用默认的Ucloud Api请求客户端
		 * 默认读取classpath下的config.setting文件。文件内容请参阅doc/config_sample.setting
		 */
		client = createUcloudApiClient();
		
		/*
		 * 创建UcloudApiClient方法二
		 * 自定义配置内容
		 */
		client = createUcloudApiClient();
		
		// 构造参数
		Param param = Param
				.create()
				.set("Action", "CreateUHostInstance")
				.set("Region", "cn-north-01")
				.set("ImageId", "f43736e1-65a5-4bea-ad2e-8a46e18883c2")
				.set("CPU", 2)
				.set("Memory", 2048)
				.set("DiskSpace", 10)
				.set("LoginMode", "Password")
				.set("Password", "UCloudexample01")
				.set("Name", "Host01")
				.set("ChargeType", "Month")
				.set("Quantity", 1);

		// 请求API，Response是个封装了返回JSON的一个对象
		Response response = client.get(param);

		// 返回的状态码
		int retCode = response.getRetCode();
		log.debug("RetCode: {}", retCode);
		// 获得原始JSON对象（使用FastJSON）
		JSONObject json = response.getJson();
		log.debug("JSON: {}", json);
		// 美化输出，更易于阅读
		String pretty = response.toPretty();
		log.debug("Pretty JSON: {}", pretty);
	}
}
