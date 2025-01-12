package com.xiaoleilu.ucloud.exception;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 参数异常
 * @author Looly
 *
 */
public class BuilderException extends RuntimeException{
	private static final long serialVersionUID = 5810833687966556207L;

	public BuilderException(Throwable e) {
		super(e);
	}
	
	public BuilderException(String message) {
		super(message);
	}
	
	public BuilderException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public BuilderException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public BuilderException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
