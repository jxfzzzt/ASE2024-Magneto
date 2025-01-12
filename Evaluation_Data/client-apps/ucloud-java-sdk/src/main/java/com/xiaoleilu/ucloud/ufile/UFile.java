package com.xiaoleilu.ucloud.ufile;

import java.io.File;
import java.io.IOException;

import jodd.http.HttpRequest;

import com.xiaoleilu.hutool.FileUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.StandardResponse;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.exception.UFileException;
import com.xiaoleilu.ucloud.util.Config;
import com.xiaoleilu.ucloud.util.HttpRequestUtil;

/**
 * 对象存储 UFile
 * @author Looly
 *
 */
public class UFile extends Ucloud{
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UFile() {
		super();
	}
	/**
	 * 构造
	 * @param config 配置文件
	 */
	public UFile(Config config) {
		super(config);
	}
	/**
	 * 构造
	 * @param client UcloudApiClient
	 */
	public UFile(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end

	/**
	 * 创建Bucket
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createBucket(Param param) {
		return client.get(UFileAction.CreateBucket, param);
	}
	
	/**
	 * 创建Bucket<br>
	 * Type： private
	 * 无绑定域名
	 * 
	 * @param bucketName Bucket名
	 * @return 返回结果
	 */
	public Response createBucket(String bucketName) {
		return createBucket(Param.create(UFileName.BucketName, bucketName));
	}

	/**
	 * 获取Bucket的描述信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeBucket(Param param) {
		return client.get(UFileAction.DescribeBucket, param);
	}

	/**
	 * 设置Bucket的属性
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response updateBucket(Param param) {
		return client.get(UFileAction.UpdateBucket, param);
	}

	/**
	 * 删除Bucket
	 * 
	 * @param bucketName 待删除Bucket的名称
	 * @return 返回结果
	 */
	public Response deleteBucket(String bucketName) {
		return client.get(UFileAction.DeleteBucket, Param.create().set(UFileName.BucketName, bucketName));
	}

	/**
	 * 获取Bucket的文件列表
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getFileList(Param param) {
		return client.get(UFileAction.GetFileList, param);
	}
	
	/**
	 * 获取Bucket的文件列表
	 * 
	 * @param bucketName Bucket名
	 * @return 返回结果
	 */
	public Response getFileList(String bucketName) {
		return getFileList(Param.create(UFileName.BucketName, bucketName));
	}
	
	/**
	 * 构建文件的URL
	 * @param bucket Bucket
	 * @param key 文件的key
	 * @return 文件的URL
	 */
	public String buildFileUrl(String bucket, String key) {
		return StrUtil.format("http://{}.ufile.ucloud.cn/{}", bucket, key);
	}
	
	/**
	 * 上传文件
	 * @param bucket Bucket
	 * @param file 文件
	 * @param key 文件在服务器上的key
	 * @param contentType 内容类型
	 * @return 响应对象
	 */
	public Response putFile(String bucket, File file, String key, String contentType) {
		byte[] fileBytes;
		try {
			fileBytes = FileUtil.readBytes(file);
		} catch (IOException e) {
			throw new UFileException(e.getMessage(), e);
		}
		
		final HttpRequest put = HttpRequestUtil.preparePut(buildFileUrl(bucket, key))
				.body(fileBytes, contentType);
		
		put.header("Authorization", Auth.build(bucket, key, "", "", client.getConfig(), put).toString());
		
		return StandardResponse.parse(put.send().bodyText());
	}
	
	/**
	 * 上传文件，上传后的文件与原文件名相同
	 * @param bucket Bucket
	 * @param file 文件
	 * @param contentType 内容类型
	 * @return 响应对象
	 */
	public Response putFile(String bucket, File file, String contentType) {
		return putFile(bucket, file, file.getName(), contentType);
	}
	
	/**
	 * 上传文件
	 * @param bucket Bucket
	 * @param file 文件
	 * @param key 文件在服务器上的key
	 * @param contentType 内容类型
	 * @return 响应对象
	 */
	public Response postFile(String bucket, File file, String key, String contentType) {
		final HttpRequest post = HttpRequestUtil.preparePost(buildFileUrl(bucket, key));
		
		post
			.contentType(contentType)
			.form("FileName", key)
			.form("Authorization", Auth.build(bucket, key, "", "", client.getConfig(), post).toString())
			.form("file", file);
		
		return StandardResponse.parse(post.send().bodyText());
	}
	
	/**
	 * 上传文件，上传后的文件与原文件名相同
	 * @param bucket Bucket
	 * @param file 文件
	 * @param contentType 内容类型
	 * @return 响应对象
	 */
	public Response postFile(String bucket, File file, String contentType) {
		return postFile(bucket, file, file.getName(), contentType);
	}
	
	/**
	 * 下载文件
	 * @param bucket Bucket
	 * @param key 文件在服务器上的key
	 * @param dest 文件
	 * @param isOverWrite 是否覆盖已有文件
	 * @return 响应对象
	 */
	public File getFile(String bucket, String key, File dest, boolean isOverWrite) {
		if(dest == null) {
			throw new UFileException("Destination file is null!");
		}
		if(dest.isDirectory()) {
			dest = new File(dest, key);
		}
		if(isOverWrite == false && dest.exists()) {
			throw new UFileException("Destination file [{}] exist!", dest.getAbsolutePath());
		}
		
		final HttpRequest get = HttpRequestUtil.prepareGet(buildFileUrl(bucket, key));
		
		get.header("Authorization", Auth.build(bucket, key, "", "", client.getConfig(), get).toString());
		
		try {
			FileUtil.writeBytes(dest, get.send().bodyBytes());
		} catch (IOException e) {
		}
		
		return dest;
	}
	
	/**
	 * 删除文件
	 * @param bucket Bucket
	 * @param key 文件在服务器上的key
	 * @return 响应对象
	 */
	public Response deleteFile(String bucket, String key) {
		final HttpRequest delete = HttpRequestUtil.prepareDelete(buildFileUrl(bucket, key));
		
		delete.header("Authorization", Auth.build(bucket, key, "", "", client.getConfig(), delete).toString());
		
		return StandardResponse.parse(delete.send().bodyText());
	}
}
