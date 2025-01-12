package com.xiaoleilu.ucloud.test;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;

import org.junit.Test;
import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.ufile.UFile;

/**
 * 对象存储 UFile测试
 * @author Looly
 *
 */
public class UFileTest {
	private final static  Logger log = Log.get();
	
	private final UFile ufile = new UFile();
	
	/**
	 * 创建Bucket测试
	 */
//	@Test
	public void createBucketTest() {
		Response res = ufile.createBucket("looly2");
		log.debug("Create Bucket: {}", res.toPretty());
	}
	
	/**
	 * 描述Bucket测试
	 */
	@Test
	public void describeBucketTest() {
		Response res = ufile.describeBucket(null);
		log.debug("Describe Bucket: {}", res.toPretty());
	}
	
	/**
	 * 删除Bucket测试
	 */
//	@Test
	public void deleteBucketTest() {
		Response res = ufile.deleteBucket("looly");
		log.debug("Delete Bucket: {}", res.toPretty());
	}
	
	/**
	 * 获取Bucket的文件列表测试
	 */
	@Test
	public void getFileListTest() {
		Response res = ufile.getFileList("looly2");
		log.debug("Get File List of Bucket: {}", res.toPretty());
	}
	
	/**
	 * 上传文件
	 */
//	@Test
	public void putFileTest() {
		Response res = ufile.putFile("looly2", new File("E:\\test.txt"), "text/plain");
		log.debug("Put File: {}", res.toPretty());
	}
	
	/**
	 * 上传文件
	 */
//	@Test
	public void postFileTest() {
		Response res = ufile.postFile("looly2", new File("E:\\test.txt"), "text/plain");
		log.debug("Post File: {}", res.toPretty());
	}
	
	/**
	 * 下载文件
	 * @throws IOException 
	 */
//	@Test
	public void getFileTest() throws IOException {
		File file = ufile.getFile("looly2", "test.txt", new File("e:\\test_download.txt"), false);
		log.debug("Get File: {}", FileUtil.readString(file));
	}
	
	/**
	 * 删除文件
	 * @throws IOException 
	 */
//	@Test
	public void deleteFileTest() throws IOException {
		Response res = ufile.deleteFile("looly2", "test.txt");
		log.debug("Get File: {}", res.toPretty());
	}
}
