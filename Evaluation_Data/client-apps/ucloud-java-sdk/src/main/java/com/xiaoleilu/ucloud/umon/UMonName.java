package com.xiaoleilu.ucloud.umon;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * UMon中使用的参数名
 * @author Looly
 *
 */
public enum UMonName implements Name{
	/** 短信内容 */
	Content,
	/** 监控指标名称 */
	MetricName,
	/** 与监控指标相关的资源ID，如主机，或其他产品 */
	ResourceId,
	/** 资源类型 */
	ResourceType,
	/** 时间步长，单位秒，默认一个小时 */
	TimeRange,
	/** 开始时间，unix timestamp */
	BeginTime,
	/** 结束时间，unix timestamp */
	EndTime
}
