package com.xiaoleilu.ucloud.test;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.uhost.UHostAction;

/**
 * UcloudBuilder使用样例
 * 
 * @author Looly
 *
 */
public class UcloudBuilderTest {
	private final static Logger log = Log.get();
	
	public static void main(String[] args) {
		//使用构建者模式创建Ucloud
		Ucloud ucloud = Ucloud
				.builder()
				.action(UHostAction.DescribeUHostInstance)
				.param("Action", "CreateUHostInstance")
				.param("Region", "cn-north-01")
				.param("ImageId", "f43736e1-65a5-4bea-ad2e-8a46e18883c2")
				.param("CPU", 2)
				.param("Memory", 2048)
				.param("DiskSpace", 10)
				.param("LoginMode", "Password")
				.param("Password", "UCloudexample01")
				.param("Name", "Host01")
				.param("ChargeType", "Month")
				.param("Quantity", 1)
				.build();
		
		//发送构建好的请求
		Response response = ucloud.send();
		
		//返回的状态码
		int retCode = response.getRetCode();
		log.debug("RetCode: {}", retCode);
		//获得原始JSON对象（使用FastJSON）
		JSONObject json = response.getJson();
		log.debug("JSON: {}", json);
		//美化输出，更易于阅读
		String pretty = response.toPretty();
		log.debug("Pretty JSON: {}",pretty);
	}
}
