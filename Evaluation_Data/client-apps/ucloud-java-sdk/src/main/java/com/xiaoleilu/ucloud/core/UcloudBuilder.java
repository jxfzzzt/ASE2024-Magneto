package com.xiaoleilu.ucloud.core;

import com.xiaoleilu.ucloud.core.Param.Name;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.exception.BuilderException;
import com.xiaoleilu.ucloud.util.Config;

/**
 * Ucloud构建器
 * @author Looly
 *
 */
public class UcloudBuilder {
	private Ucloud ucloud;

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UcloudBuilder() {
		this.ucloud = new Ucloud();
	}

	/**
	 * 构造
	 * 
	 * @param config 配置文件
	 */
	public UcloudBuilder(Config config) {
		this.ucloud = new Ucloud(config);
	}

	/**
	 * 构造
	 * 
	 * @param client UcloudApiClient
	 */
	public UcloudBuilder(UcloudApiClient client) {
		this.ucloud = new Ucloud(client);
	}
	// --------------------------------------------------------------- Constructor end
	
	/**
	 * 第一步，设置指令名称
	 * @param action 指令名称
	 * @return 自己
	 */
	public UcloudBuilder action(Action action) {
		this.ucloud.param = Param.create(PubName.Action, action);
		return this;
	}
	
	/**
	 * 第二步，设置数据中心
	 * @param region 数据中心
	 * @return 自己
	 */
	public UcloudBuilder region(Region region) {
		validateParam();
		
		this.ucloud.param.set(PubName.Region, region);
		return this;
	}
	
	/**
	 * 第三步，设置参数
	 * @param name 参数名
	 * @param value 参数值
	 * @return 自己
	 */
	public UcloudBuilder param(String name, Object value) {
		validateParam();
		
		this.ucloud.param.set(name, value);
		return this;
	}
	
	/**
	 * 第三步，设置参数
	 * @param name 参数名
	 * @param value 参数值
	 * @return 自己
	 */
	public UcloudBuilder param(Name name, Object value) {
		validateParam();
		
		this.ucloud.param.set(name, value);
		return this;
	}
	
	/**
	 * 第三步，设置参数
	 * @param param 参数
	 * @return 自己
	 */
	public UcloudBuilder param(Param param) {
		validateParam();
		
		this.ucloud.param.setAll(param);
		return this;
	}
	
	/**
	 * 第四步，构建Ucloud客户端
	 * @return 返回的结果
	 */
	public Ucloud build() {
		validateParam();
		
		return this.ucloud;
	}
	
	/**
	 * 验证参数
	 */
	private void validateParam() {
		if(this.ucloud.param == null || this.ucloud.param.isEmpty()) {
			throw new BuilderException("Please call action() method first to specified action!");
		}
	}
}
