package com.xiaoleilu.ucloud.umon;

/**
 * 监控指标名称
 * @author Looly
 *
 */
public enum MetricName {
	NetworkIn,
	NetworkOut,
	CPUUtilization,
	IORead,
	IOWrite,
	DiskReadOps,
	NICIn,
	NICOut,
	MemUsage,
	DataSpaceUsage,
	RootSpaceUsage,
	ReadonlyDiskCount,
	RunnableProcessCount,
	BlockProcessCount,
	
	QPS,
	ExpensiveQuery,
	
	TotalNetworkOut,
	CurrentConnections,
	
	Usage,
	InstanceCount
}
