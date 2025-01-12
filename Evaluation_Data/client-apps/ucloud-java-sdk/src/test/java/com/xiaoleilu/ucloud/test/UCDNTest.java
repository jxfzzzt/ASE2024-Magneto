package com.xiaoleilu.ucloud.test;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.ucdn.UCDN;

/**
 * UCDN样例
 * @author Looly
 *
 */
public class UCDNTest {
	private final static  Logger log = Log.get();
	
	public static void main(String[] args) {
		final UCDN ucdn = new UCDN();
		
		//获取流量信息
		Response resp = ucdn.getUcdnTraffic();
		log.debug("UCDN Traffic: {}", resp.toPretty());
	}
}
