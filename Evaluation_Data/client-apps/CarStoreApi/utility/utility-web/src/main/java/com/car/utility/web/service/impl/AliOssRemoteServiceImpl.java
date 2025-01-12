package com.car.utility.web.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.car.utility.web.common.utils.AliOssClient;
import com.car.utility.web.service.AliOssRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xlj
 *
 */
@Service
@Slf4j
public class AliOssRemoteServiceImpl implements AliOssRemoteService {

	@Override
	public Boolean put(String fileName, InputStream inputStream, String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		OSSClient client = AliOssClient.getInstance(endpoint, accessKeyId, accessKeySecret);
		try {
			client.putObject(bucketName, fileName, inputStream);
		} catch (OSSException e) {
			log.error("oss exception:" + e.getMessage(), e);
			return Boolean.FALSE;
		} catch (ClientException e) {
			log.error("oss client:" + e.getMessage(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public List<String> listFile(String filePath, String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		OSSClient client = AliOssClient.getInstance(endpoint, accessKeyId, accessKeySecret);
		List<String> fileNameList = new ArrayList<>();
		try {
			ObjectListing objectListingList = client.listObjects(bucketName, filePath);
			for (OSSObjectSummary objectSummary : objectListingList.getObjectSummaries()) {
				fileNameList.add(objectSummary.getKey());
			}
		} catch (OSSException e) {
			log.error("oss exception:" + e.getMessage(), e);
			return Collections.emptyList();
		} catch (ClientException e) {
			log.error("oss client:" + e.getMessage(), e);
			return Collections.emptyList();
		}
		return fileNameList;
	}

	@Override
	public void delete(String fileName, String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		OSSClient client = AliOssClient.getInstance(endpoint, accessKeyId, accessKeySecret);
		try {
			client.deleteObject(bucketName, fileName);
		} catch (OSSException e) {
			log.error("oss exception:" + e.getMessage(), e);
		} catch (ClientException e) {
			log.error("oss client:" + e.getMessage(), e);
		}
	}
}
