package com.xiaoleilu.ucloud.unet.security;

/**
 * 防火墙动作
 * @author Looly
 *
 */
public enum SecurityAction {
	/** 允许通过防火墙 */
	ACCEPT,
	/** 禁止通过防火墙并不给任何返回信息 */
	DROP,
}
