package com.xiaoleilu.ucloud.ucdn;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 接入云 UCDN API指令
 * @author Looly
 *
 */
public enum UCDNAction implements Action{
	/** 购买流量 */
    BuyUcdnTraffic,
    /** 获取流量信息 */
    GetUcdnTraffic,
    /** 创建加速域名 */
    CreateUcdnDomain,
    /** 更新加速域名配置 */
    UpdateUcdnDomain,
    /** 获取加速域名详细信息 */
    DescribeUcdnDomain,
    /** 获取加速域名带宽使用信息 */
    GetUcdnDomainBandwidth,
    /** 获取加速域名流量使用信息 */
    GetUcdnDomainTraffic,
    /** 获取加速域名原始日志 */
    GetUcdnDomainLog,
    
    /** 刷新加速缓存 */
    RefreshUcdnDomainCache,
    /** 获取域名刷新任务状态 */
    DescribeRefreshCacheTask,
    /** 预取文件 */
    PrefetchDomainCache,
    /** 获取域名预取任务状态 */
    DescribePrefetchCacheTask,
    /** 更新加速域名状态 */
    UpdateUcdnDomainStatus,
    /** 获取域名预取开启状态 */
    GetUcdnDomainPrefetchEnable
}
