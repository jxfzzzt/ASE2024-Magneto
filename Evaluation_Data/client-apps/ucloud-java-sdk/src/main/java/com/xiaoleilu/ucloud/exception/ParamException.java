package com.xiaoleilu.ucloud.exception;

import com.xiaoleilu.hutool.StrUtil;

/**
 * 参数异常
 * @author Looly
 *
 */
public class ParamException extends RuntimeException{
	private static final long serialVersionUID = 5810833687966556207L;

	public ParamException(Throwable e) {
		super(e);
	}
	
	public ParamException(String message) {
		super(message);
	}
	
	public ParamException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}
	
	public ParamException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ParamException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}
}
