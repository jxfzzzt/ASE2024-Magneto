package com.xiaoleilu.ucloud.test;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.udb.ClassType;
import com.xiaoleilu.ucloud.udb.UDB;
import com.xiaoleilu.ucloud.udb.UDBName;

/**
 * UDB样例
 * @author Looly
 *
 */
public class UDBTest {
	private final static  Logger log = Log.get();
	
	public static void main(String[] args) {
		final UDB udb = new UDB();
		
		//获取udb实例信息
		Param param = Param.create()
				.setRegion(Region.CN_NORTH_03)
				.set(UDBName.ClassType, ClassType.SQL)
				.setOffset(0)
				.setLimit(10);
		Response resp = udb.describeUDBInstance(param);
		log.debug("UDB Instance: {}", resp.toPretty());
	}
}
