package com.xiaoleilu.ucloud.exception;

import com.xiaoleilu.hutool.StrUtil;

/**
 * UFile异常
 * @author Looly
 *
 */
public class UFileException extends RuntimeException{
	private static final long serialVersionUID = -7140101712183457081L;

	public UFileException(Throwable e) {
		super(e);
	}
	
	public UFileException(String message) {
		super(message);
	}
	
	public UFileException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public UFileException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public UFileException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
