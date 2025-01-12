package com.xiaoleilu.ucloud.uhost;

/**
 * 认证方式
 * @author Looly
 *
 */
public enum LoginMode {
	/** 密码 */
	Password,
	/** KeyPair（暂不支持） */
	@Deprecated
	KeyPair
}
