package com.car.utility.web.service;

import java.io.InputStream;
import java.util.List;

/**
 * aliOSS remove service
 * @author xielinjiang
 *
 */
public interface AliOssRemoteService {

	/**
	 * simple upload
	 * @param fileName
	 * @param inputStream
	 * @param endpoint
	 * @param accessKeyId
	 * @param accessKeySecret
	 * @return
	 */
	Boolean put(String fileName, InputStream inputStream, String endpoint, String accessKeyId, String accessKeySecret, String bucketName);

	/**
	 * list file in direction
	 * @param filePath
	 * @return
	 */
	List<String> listFile(String filePath, String endpoint, String accessKeyId, String accessKeySecret, String bucketName);

	/**
	 * delete file
	 * @param fileName
	 */
	void delete(String fileName, String endpoint, String accessKeyId, String accessKeySecret, String bucketName);
}
