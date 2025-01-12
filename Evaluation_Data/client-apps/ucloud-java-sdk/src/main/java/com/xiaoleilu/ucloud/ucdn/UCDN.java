package com.xiaoleilu.ucloud.ucdn;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 云CDN
 * 
 * @author Looly
 *
 */
public class UCDN extends Ucloud {
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UCDN() {
		super();
	}
	/**
	 * 构造
	 * @param config 配置文件
	 */
	public UCDN(Config config) {
		super(config);
	}
	/**
	 * 构造
	 * @param client UcloudApiClient
	 */
	public UCDN(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end

	/**
	 * 购买流量
	 * 
	 * @param traffic 所购买的流量, 单位GB
	 * @param areacode 购买流量的区域
	 * @return 返回结果
	 */
	public Response buyUcdnTraffic(int traffic, Areacode areacode) {
		Param param = Param.create()
				.set(UCDNName.Traffic, traffic)
				.set(UCDNName.Areacode, areacode);
		return client.get(UCDNAction.BuyUcdnTraffic, param);
	}

	/**
	 * 获取流量信息
	 * 
	 * @return 返回结果
	 */
	public Response getUcdnTraffic() {
		return client.get(UCDNAction.GetUcdnTraffic, Param.create());
	}

	/**
	 * 创建加速域名<br>
	 * 创建加速域名之前, 需要先购买流量, 并且国内加速只能使用国内流量, 海外加速只能使用海外流量.
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUcdnDomain(Param param) {
		return client.get(UCDNAction.CreateUcdnDomain, param);
	}

	/**
	 * 更新加速域名配置
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response updateUcdnDomain(Param param) {
		return client.get(UCDNAction.UpdateUcdnDomain, param);
	}

	/**
	 * 获取加速域名详细信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUcdnDomain(Param param) {
		return client.get(UCDNAction.DescribeUcdnDomain, param);
	}

	/**
	 * 获取加速域名带宽使用信息<br>
	 * 带宽使用数据最长保留一个月的时间。
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getUcdnDomainBandwidth(Param param) {
		return client.get(UCDNAction.GetUcdnDomainBandwidth, param);
	}

	/**
	 * 获取加速域名流量使用信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getUcdnDomainTraffic(Param param) {
		return client.get(UCDNAction.GetUcdnDomainTraffic, param);
	}

	/**
	 * 获取加速域名原始日志
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getUcdnDomainLog(Param param) {
		return client.get(UCDNAction.GetUcdnDomainLog, param);
	}

	/**
	 *  刷新加速缓存
	 * @param domainId 域名ID，创建加速域名时生成。
	 * @param refreshType 刷新类型
	 * @param urlList 刷新的URL列表，一次最多提交30个。必须以”http://域名/”开始。目录要以”/”结尾， 如刷新目录a下所有文件，格式为：http://abc.ucloud.cn/a/；如刷新文件目录a下面所有img.png文件， 格式为http://abc.ucloud.cn/a/img.png。请正确提交需要刷新的域名
	 * @return 返回结果
	 */
	public Response refreshUcdnDomainCache(String domainId, RefreshType refreshType, String... urlList) {
		Param param = Param.create()
				.set(UCDNName.DomainId, domainId)
				.set(UCDNName.Type, refreshType);

		// add urls
		for (int i = 0; i < urlList.length; i++) {
			param.set(StrUtil.format("{}.{}", UCDNName.UrlList, i), urlList[i]);
		}

		return client.get(UCDNAction.RefreshUcdnDomainCache, param);
	}

	/**
	 * 获取域名刷新任务状态
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeRefreshCacheTask(Param param) {
		return client.get(UCDNAction.DescribeRefreshCacheTask, param);
	}

	/**
	 * 预取文件
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response prefetchDomainCache(Param param) {
		return client.get(UCDNAction.PrefetchDomainCache, param);
	}

	/**
	 * 获取域名预取任务状态
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describePrefetchCacheTask(Param param) {
		return client.get(UCDNAction.DescribePrefetchCacheTask, param);
	}

	/**
	 * 更新加速域名状态<br>
	 * 目前支持暂停和启用加速域名，删除需要人工实现（审核失败支持页面删除操作）。
	 * 
	 * @param domainId 域名ID，创建加速域名时生成。
	 * @param status 域名状态
	 * @return 返回结果
	 */
	public Response updateUcdnDomainStatus(String domainId, DomainStatus status) {
		Param param = Param.create()
				.set(UCDNName.DomainId, domainId)
				.set(UCDNName.Status, status);
		return client.get(UCDNAction.UpdateUcdnDomainStatus, param);
	}

	/**
	 * 获取域名预取开启状态<br>
	 * 网页加速开启预取需联系技术支持，大文件下载、点播默认开启预取。
	 * 
	 * @param domainId 域名ID，创建加速域名时生成。
	 * @return 返回结果
	 */
	public Response getUcdnDomainPrefetchEnale(String domainId) {
		return client.get(UCDNAction.GetUcdnDomainPrefetchEnable, Param.create().set(UCDNName.DomainId, domainId));
	}

}
