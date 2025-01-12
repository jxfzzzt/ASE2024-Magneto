package com.xiaoleilu.ucloud.util;

import com.xiaoleilu.hutool.Setting;
import com.xiaoleilu.ucloud.exception.ConfigException;


/**
 * 公共参数设置<br>
 * 公钥和私钥，获取地址：https://account.ucloud.cn/account/user#api_key
 * @author Looly
 *
 */
public class Config implements Cloneable{
	
	/** 默认的公共参数设置文件路径 */
	private final static String DEFAULT_CONFIG_PATH = "config.setting";
	
	/** 公钥 */
	private String publicKey;
	/** 私钥 */
	private String privateKey;
	/** API的URL */
	private String baseUrl;
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造公共参数对象
	 * 
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * @param baseUrl 请求API的URL
	 */
	public Config(String publicKey, String privateKey, String baseUrl) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.baseUrl = baseUrl;
	}
	
	/**
	 * 构造公共参数对象<br>
	 * 使用默认请求API的URL
	 * 
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 */
	public Config(String publicKey, String privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.baseUrl = Global.DEFAULT_BASE_URL;
	}
	// --------------------------------------------------------------- Constructor end
	
	// --------------------------------------------------------------- Getters and Setters start
	/**
	 * @return 公钥
	 */
	public String getPublicKey() {
		return publicKey;
	}
	/**
	 * 设置公钥<br>
	 * 公钥格式类似于ucloudsomeone@example.com1296235120854146120
	 * @param publicKey 公钥
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return 私钥
	 */
	public String getPrivateKey() {
		return privateKey;
	}
	/**
	 * 设置私钥<br>
	 * 私钥格式类似于46f09bb9fab4f12dfc160dae12273d5332b5debe
	 * @param privateKey 私钥
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return 请求API的基本路径
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	/**
	 * 设置请求API的基本路径
	 * @param baseUrl 请求API的基本路径
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	// --------------------------------------------------------------- Getters and Setters end
	
	/**
	 * 克隆设置
	 * @return 克隆后的对象
	 */
	public Config clone() {
		try {
			return (Config)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	// --------------------------------------------------------------- Static Method start
	/**
	 * 从配置文件文件中读取公共参数
	 * @see com.xiaoleilu.hutool.Setting
	 * @param setting 配置文件
	 * @return 公共参数
	 */
	public static Config createFromSetting(Setting setting){
		return new Config(setting.getString("public_key"), setting.getString("private_key"), setting.getString("base_url"));
	}
	
	/**
	 * 从默认配置文件文件中读取公共参数<br>
	 * 默认配置文件为config.setting，格式见config_sample.setting<br>
	 * 配置文件放于项目根目录下既可，如果为Web项目，放于classes目录下既可<br>
	 * 如果为Maven项目，放于src/main/resources下既可自动部署至相应目录。
	 * @return 公共参数
	 */
	public static Config createFromSetting(){
		Setting setting = null;
		try {
			setting = new Setting(DEFAULT_CONFIG_PATH);
		} catch (Exception e) {
			throw new ConfigException(e.getMessage(), e);
		}
		return createFromSetting(setting);
	}
	// --------------------------------------------------------------- Static Method end
	
}
