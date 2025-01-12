package com.xiaoleilu.ucloud.ufile;

import java.util.Map;
import java.util.Map.Entry;

import jodd.http.HttpRequest;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.util.Config;
import com.xiaoleilu.ucloud.util.SignatureUtil;

/**
 * 认证器
 * @author Looly
 *
 */
public class Auth implements Cloneable{
	private static final Logger log = Log.get();
	
	// --------------------------------------------------------------- Static method start
	/**
	 * 创建认证器
	 * @return 认证器
	 */
	public static Auth build() {
		return new Auth();
	}
	
	/**
	 * 创建认证器
	 * @param bucket Bucket
	 * @param key 文件key
	 * @param contentMd5 内容MD5
	 * @param date 日期
	 * @param config 配置文件
	 * @param request Http请求对象
	 */
	public static Auth build(String bucket, String key, String contentMd5, String date, Config config, HttpRequest request) {
		return new Auth(bucket, key, contentMd5, date, config, request);
	}

	/**
	 * 用于签名的字符串
	 * 
	 * @param bucket Bucket
	 * @param key 文件key
	 * @param contentMd5 内容的md5值
	 * @param date 日期
	 * @param request 请求对象
	 * @return 用于签名的字符串
	 */
	public static String strToSign(String bucket, String key, String contentMd5, String date, HttpRequest request) {
		String contentType = request.contentType();
		if(StrUtil.isBlank(contentType)) {
			contentType = "text/plain";
			request.contentType(contentType);
			log.warn("Content-Type header is empty, use default Content-Type: {}", contentType);
		}
		
		return StrUtil
				.builder()
				.append(request.method())
				.append("\n")
				.append(StrUtil.nullToEmpty(contentMd5))
				.append("\n")
				.append(contentType)
				.append("\n")
				.append(StrUtil.nullToEmpty(date))
				.append("\n")
				.append(canonicalizedUcloudHeaders(request))
				// canonicalizedUcloudHeaders尾部带一个换行符
				.append(canonicalizedResource(bucket, key))
				.toString();
	}

	/**
	 * 签名
	 * 
	 * @param privateKey 私钥
	 * @param strToSign 被签名的字符串
	 * @return 签名
	 */
	public static String sign(String privateKey, String strToSign) {
		return SignatureUtil.macSign(privateKey, strToSign);
	}

	/**
	 * 认证字符串
	 * 
	 * @param publicKey 公钥
	 * @param signature 签名
	 * @return 认证字符串
	 */
	public static String authorization(String publicKey, String signature) {
		return StrUtil.builder().append("UCloud ").append(publicKey).append(":").append(signature).toString();
	}
	
	/**
	 * 用于签名的标准Ucloud头信息字符串，尾部带换行符
	 * 
	 * @param request Http请求
	 * @return 用于签名的Ucloud头信息字符串
	 */
	public static String canonicalizedUcloudHeaders(HttpRequest request) {
		Param param = Param.create();

		Map<String, String[]> headers = request.headers();
		String[] value;
		for (Entry<String, String[]> headerEntry : headers.entrySet()) {
			String name = headerEntry.getKey().toLowerCase();
			if (name.startsWith("x-ucloud-")) {
				value = headerEntry.getValue();
				if (value != null && value.length > 0) {
					param.set(name, value[0]);
				}
			}
		}

		StringBuilder builder = StrUtil.builder();
		for (Entry<String, Object> entry : param.entrySet()) {
			builder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
		}

		return builder.toString();
	}

	/**
	 * 用于签名的标准资源字符串
	 * 
	 * @param bucket Bucket
	 * @param key 文件的key（在Ufile中的文件名）
	 * @return 标准资源字符串
	 */
	public static String canonicalizedResource(String bucket, String key) {
		return StrUtil.builder().append("/").append(bucket).append("/").append(key).toString();
	}
	// --------------------------------------------------------------- Static method end
	
	// --------------------------------------------------------------- Field start
	/** Bucket */
	private String bucket;
	/** 文件在Bucket中的唯一名 */
	private String key;
	/** 文件的MD5 */
	private String contentMd5 = StrUtil.EMPTY;
	/** 日期 */
	private String date = StrUtil.EMPTY;
	/** 配置文件 */
	private Config config;
	/** Http请求对象 */
	private HttpRequest request;
	// --------------------------------------------------------------- Field end
	
	/**
	 * 构造
	 */
	public Auth() {
	}
	
	/**
	 * 构造
	 * @param bucket Bucket
	 * @param key 文件key
	 * @param contentMd5 内容MD5
	 * @param date 日期
	 * @param config 配置文件
	 * @param request Http请求对象
	 */
	public Auth(String bucket, String key, String contentMd5, String date, Config config, HttpRequest request) {
		super();
		this.bucket = bucket;
		this.key = key;
		this.contentMd5 = contentMd5;
		this.date = date;
		this.config = config;
		this.request = request;
	}

	// --------------------------------------------------------------- Getters and Setters start
	/**
	 * 获得Bucket
	 * @return Bucket
	 */
	public String getBucket() {
		return bucket;
	}
	/**
	 * 设置Bucket
	 * @param bucket Bucket
	 */
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	/**
	 * @return 文件key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 设置文件key
	 * @param key 文件key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return 文件内容MD5
	 */
	public String getContentMd5() {
		return contentMd5;
	}
	/**
	 * 设置文件内容MD5
	 * @param contentMd5 文件内容MD5
	 */
	public void setContentMd5(String contentMd5) {
		this.contentMd5 = contentMd5;
	}

	/**
	 * @return 日期
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 设置日期
	 * @param date 日期
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return 配置文件
	 */
	public Config getConfig() {
		return config;
	}
	/**
	 * 设置配置文件
	 * @param config 配置文件
	 */
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * @return Http请求对象
	 */
	public HttpRequest getRequest() {
		return request;
	}
	/**
	 * 设置Http请求对象
	 * @param request Http请求对象
	 */
	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	// --------------------------------------------------------------- Getters and Setters end
	
	/**
	 * 生成认证字符串
	 */
	@Override
	public String toString() {
		final String strToSign = Auth.strToSign(bucket, key, contentMd5, date, request);
		final String signature = Auth.sign(config.getPrivateKey(), strToSign);
		final String authorization = Auth.authorization(config.getPublicKey(), signature);
		
		return authorization;
	}
	
	/**
	 * 克隆对象
	 */
	public Auth clone() {
		try {
			return (Auth) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
