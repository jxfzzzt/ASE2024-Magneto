package com.xiaoleilu.ucloud.exception;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 公共参数设置异常
 * @author Looly
 *
 */
public class ConfigException extends RuntimeException{
	private static final long serialVersionUID = -5134242152486033265L;

	public ConfigException(Throwable e) {
		super(e);
	}
	
	public ConfigException(String message) {
		super(message);
	}
	
	public ConfigException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public ConfigException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ConfigException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
