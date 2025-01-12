package com.xiaoleilu.ucloud.test;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.ulb.ULB;

/**
 * ULB样例
 * @author Looly
 *
 */
public class ULBTest {
	private final static  Logger log = Log.get();
	
	public static void main(String[] args) {
		final ULB ulb = new ULB();
		
		//获取ULB详细信息
		Param param = Param.create()
				.setRegion(Region.CN_NORTH_03)
				.setOffset(0)
				.setLimit(10);
		Response resp = ulb.describeULB(param);
		log.debug("ULB Instance: {}", resp.toPretty());
	}
}
