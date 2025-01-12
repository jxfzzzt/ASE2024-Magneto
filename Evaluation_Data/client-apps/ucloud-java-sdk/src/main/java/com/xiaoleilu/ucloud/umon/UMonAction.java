package com.xiaoleilu.ucloud.umon;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 云监控 UMon API指令
 * @author Looly
 *
 */
public enum UMonAction implements Action{
	/** 获取监控数据 */
	GetMetric,
	/** 发送短信 */
	SendSms
}
