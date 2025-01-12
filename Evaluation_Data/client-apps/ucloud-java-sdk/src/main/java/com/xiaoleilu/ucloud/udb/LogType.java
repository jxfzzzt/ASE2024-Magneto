package com.xiaoleilu.ucloud.udb;

/**
 * 日志类型
 * @author Looly
 *
 */
public enum LogType {
	/** 暂不支持 */
	@Deprecated
	error("10-error"),
	/** 暂不支持 */
	@Deprecated
	slow("20-slow"),
	binlog("30-binlog");
	
	private String value;
	
	private LogType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}
