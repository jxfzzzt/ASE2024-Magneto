package com.xiaoleilu.ucloud.core.enums;

/**
 * 计费模式
 * @author Looly
 *
 */
public enum ChargeType {
	/** 按年付费 */
	Year,
	/** 按月付费 */
	Month,
	/** 按需付费（需开启权限） */
	Dynamic,
	/** 试用（需开启权限） */
	Trial
}
