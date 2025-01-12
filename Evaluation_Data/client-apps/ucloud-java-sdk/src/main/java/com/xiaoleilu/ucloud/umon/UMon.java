package com.xiaoleilu.ucloud.umon;

import java.util.Arrays;
import java.util.Collection;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.Validator;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 云监控
 * @author Looly
 *
 */
public class UMon extends Ucloud{

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UMon() {
		super();
	}
	/**
	 * 构造
	 * @param config 配置文件
	 */
	public UMon(Config config) {
		super(config);
	}
	/**
	 * 构造
	 * @param client UcloudApiClient
	 */
	public UMon(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end
	
	/**
	 * 发送短信<br>
	 * 1. 短信内容无论字母，汉字，中英文标点符号，均按照1个字符计算，内容长度不能多于600字。 <br>
	 * 2. 增加内容:超过70字的短信，按照每条65字收取费用。<br>
	 * @param content 短信内容
	 * @param phoneNumbers 短信列表
	 * @return 响应内容
	 */
	public Response sendSms(String content, Collection<String> phoneNumbers){
		Param param = Param.create()
				.set(UMonName.Content, content);
		
		int i = 0;
		for (String phoneNumber : phoneNumbers) {
			if(StrUtil.isNotBlank(phoneNumber) && Validator.isMobile(phoneNumber)) {
				param.set("Phone." + i, phoneNumber);
				i++;
			}else {
				log.warn("{} is not phone number!", phoneNumber);
			}
		}
		
		return client.get(UMonAction.SendSms, param);
	}
	
	/**
	 * 发送短信<br>
	 * 1. 短信内容无论字母，汉字，中英文标点符号，均按照1个字符计算，内容长度不能多于600字。 <br>
	 * 2. 增加内容:超过70字的短信，按照每条65字收取费用。<br>
	 * @param content 短信内容
	 * @param phoneNumbers 短信列表
	 * @return 响应内容
	 */
	public Response sendSms(String content, String... phoneNumbers){
		return sendSms(content, Arrays.asList(phoneNumbers));
	}
	
	/**
	 * 获取监控数据
	 * @param param 参数
	 * @return 结果
	 */
	public Response getMetric(Param param) {
		return client.get(UMonAction.GetMetric, param);
	}
}
