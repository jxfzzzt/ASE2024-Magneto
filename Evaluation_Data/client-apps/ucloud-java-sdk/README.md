[![BSD License](http://img.shields.io/hexpm/l/plug.svg)](https://gitcafe.com/looly/ucloud-java-sdk)
[![Build Status](http://img.shields.io/travis/joyent/node/v0.6.svg)](https://gitcafe.com/looly/ucloud-java-sdk)

![封面](https://gitcafe-image.b0.upaiyun.com/c6f592a6a94231bd62b5da91510dcf0a.jpg)

# Ucloud-java-sdk

ucloud-java-sdk是Ucloud官方API的Java封装，此SDK不但提供了接口的完整封装，还提供了一些自动化运维和自动化伸缩的相关功能。

## 功能特点

1. 使用`Param`对象做为大多数SDK API的接口参数，摒弃`Bean`类型参数造成的灵活性不足
2. 优雅的代码注释，为每个类、方法和枚举对象提供文档级别的注释信息，在通过Maven配合IDE的情况下最大限度减少文档的查阅。
3. 代码提交至[Maven](http://maven.apache.org/)中央库，方便[Maven](http://maven.apache.org/)以及[Gradle](http://gradle.org/)项目的引用（再也不用到处找Jar引入ClassPath了）
4. 部分功能完整的做单元测试，确保代码可用，而且单元测试类便是Example，降低学习门槛。
5. `UcloudApiClient`类提供公共客户端，在API新增或变动方法时，旧版SDK依旧可以调用新接口。
6. 灵活的配置：`Config`对象可以使用默认的`config.setting`，也可以使用自定义路径的配置文件，也可以使用`Config`对象在代码中指定参数（私钥、公钥等），为不同的项目提供高度可定制的功能。
7. 提供灵活的`Response`。由于服务器返回JSON，SDK将JSON包装为Response，封装一些返回的JSON中公共的方法，其它则为对JSON的操作，大大提高灵活性。
8. 详细的文档，为开发者提供良好的使用体验。

## 引入SDK

### Maven
在项目的pom.xml的dependencies中加入以下内容:

```XML
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>ucloud-java-sdk</artifactId>
    <version>X.X.X</version>
</dependency>
```

注：ucloud-java-sdk的版本可以通过 http://search.maven.org/ 搜索`ucloud-java-sdk`找到项目。

### 非Maven项目
可以从[http://search.maven.org/](http://search.maven.org/) 搜索`ucloud-java-sdk`找到项目，点击对应版本，下面是相应的Jar包，导入即可使用。

![](https://gitcafe-image.b0.upaiyun.com/5f9edb85807f28fe57119f61822650c7.png)

## 配置

私钥、公钥等信息可以在配置文件中设置，如果你使用的是默认的Ucloud Api请求客户端（`new UcloudApiClient()`），那可以在classpath下新建`config.setting`文件。文件内容请参阅`doc/config_sample.setting`

## 开始
使用SDK调用相应功能有三种方法，推荐第三种。

### 方法一：使用UcloudApiClient

```Java
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
```

### 方法二：构建者模式创建请求
```Java
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
```

### 方法三：使用相应功能的类
每个功能都有相应的类对应，创建响应对象，调用对象方法即可，详细请看Wiki。

## 详细文档请参阅Wiki：

[https://gitcafe.com/looly/ucloud-java-sdk/wiki/pages](https://gitcafe.com/looly/ucloud-java-sdk/wiki/pages)