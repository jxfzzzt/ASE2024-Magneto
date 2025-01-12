package com.car.utility.web.common.utils;

import com.aliyun.oss.OSSClient;
import org.springframework.stereotype.Service;

/**
 * ali OSS singleton
 * @author xlj
 *
 */
@Service
public class AliOssClient {

	private static volatile OSSClient client = null;

	private AliOssClient() {
		// exists only on defeat instantiation
	}

	public static synchronized OSSClient getInstance(String endpoint, String accessKeyId, String accessKeySecret) {
		if (null == client) {
			synchronized (AliOssClient.class) {
				if (null == client) {
					client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
				}
			}
		}
		return client;
	}

}
