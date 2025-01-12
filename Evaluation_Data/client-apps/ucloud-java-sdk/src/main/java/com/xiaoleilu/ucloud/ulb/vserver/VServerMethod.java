package com.xiaoleilu.ucloud.ulb.vserver;

/**
 * VServer负载均衡模式
 * @author Looly
 *
 */
public enum VServerMethod {
	/** 轮询 */
	Roundrobin,
	/** 源地址 */
	Source
}
