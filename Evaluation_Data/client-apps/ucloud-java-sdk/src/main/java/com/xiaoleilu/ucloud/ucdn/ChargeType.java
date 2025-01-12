package com.xiaoleilu.ucloud.ucdn;

/**
 * UCDN计费方式
 * @author Looly
 *
 */
public enum ChargeType {
	/** 按流量包计费 */
	traffic,
	/** 按带宽计费 */
	@Deprecated
	bandwidth,
	/** 代表流量后付费 */
	@Deprecated
	trafficused
}
