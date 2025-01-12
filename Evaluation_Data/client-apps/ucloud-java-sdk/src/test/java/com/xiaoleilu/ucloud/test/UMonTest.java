package com.xiaoleilu.ucloud.test;

import org.junit.Test;
import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.core.enums.ResourceType;
import com.xiaoleilu.ucloud.umon.MetricName;
import com.xiaoleilu.ucloud.umon.UMon;
import com.xiaoleilu.ucloud.umon.UMonName;

/**
 * 云监控 测试类
 * @author Looly
 *
 */
public class UMonTest {
	private final static  Logger log = Log.get();
	
	private final UMon uMon = new UMon();
	
	/**
	 * 发送短信测试
	 */
//	@Test
	public void sendSmdTest(){
		Response resp = uMon.sendSms("测试短信", "18801050000");
		log.debug("Send Sms: {}", resp.toPretty());
	}
	
	/**
	 * 监控信息
	 */
	@Test
	public void getMetricTest() {
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(UMonName.MetricName + ".0", MetricName.CPUUtilization)
				.set(UMonName.ResourceId, "uhost-agd0gk")
				.set(UMonName.ResourceType, ResourceType.uhost)
				.set(UMonName.TimeRange, 1000);
		Response res = uMon.getMetric(param);
		
		log.debug("Metric: {}", res.toPretty());
	}
}
