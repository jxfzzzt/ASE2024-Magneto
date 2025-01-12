package com.xiaoleilu.ucloud.ucdn;

/**
 * 需要获取的内容刷新的状态
 * @author Looly
 *
 */
public enum RefreshStatus {
	/** 成功 */
	success,
	/** 等待处理 */
	wait,
	/** 正在处理 */
	process,
	/** 失败 */
	failure,
	/** 未知 */
	unkonw
}
