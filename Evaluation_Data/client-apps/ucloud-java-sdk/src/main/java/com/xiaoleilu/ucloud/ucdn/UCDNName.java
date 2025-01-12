package com.xiaoleilu.ucloud.ucdn;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * UCDN中使用的参数名
 * @author Looly
 *
 */
public enum UCDNName implements Name{
	/** 缓存文件或路径需要缓存的时间。单位：秒 */
	CacheTtl,
	/** 不需要缓存的文件或路径。格式同CacheUrls */
	NoCacheUrl,
	/** 加速域名的业务类型，web代表网站，stream代表视频，download代表下载，Live代表直播 */
	CdnType,
	/** 
	 * 刷新多个URL列表时，一次最多提交30个。<br>
	 * 必须以”http://域名/”开始。目录要以”/”结尾， 如刷新目录a下所有文件，格式为：http://abc.ucloud.cn/a/<br>
	 * 如刷新文件目录a下面所有img.png文件， 格式为http://abc.ucloud.cn/a/img.png。请正确提交需要刷新的域名 
	 */
	UrlList,
	/** None */
	不需要提供参数,
	/** 
	 * 计费方式。默认使用流量包计费。<br>
	 * 枚举值为：<br>
	 * traffic：按流量包计费；<br>
	 * bandwidth：按带宽计费；<br>
	 * trafficused：代表流量后付费。（目前仅支持按流量包计费）
	 */
	ChargeType,
	/** 域名ID，创建加速域名时生成。 */
	DomainId,
	/** 
	 * CDN加速区域，目前区域代表有：<br>
	 * cn：国内；abroad：国外。<br>
	 * 可选择多个区域，表述为：”Areacodes.0=cn, Areacodes.1=aboard” 表示同时使用国内和海外节点 <br>
	 */
	Areacodes,
	/** 
	 * 需要获取的内容刷新的状态<br>
	 * 枚举值：<br>
	 * success：成功；wait：等待处理；process：正在处理；failure：失败；unkonw：未知<br>
	 * 默认选择所有状态<br>
	 */
	Status,
	/** 源站IP，即cdn服务器回源访问的IP地址。支持多个源站IP。<br>
	 * 多个源站IP可以表述为：<br>
	 * SourceIps.0=1.1.1.1，SourceIps.1=2.2.2.2
	 */
	SourceIp,
	/** 购买流量的区域, 枚举值为:cn: 国内; abroad: 海外 */
	Areacode,
	/** 测试url，用于域名创建加速时的测试。
	（如果CdnType为live，则该字段非必须，
	否则该字段为必须字段） */
	TestUrl,
	/** 查询的结束时间，格式为Unix Timestamp。EndTime默认为当前时间，BeginTime默认为当前时间前一天时间。 */
	EndTime,
	/** 用于加速的域名 */
	Domain,
	/** 是否按天展示带宽峰值，枚举值：0：否；1：是；默认为0 */
	Daily,
	/** 所购买的流量, 单位GB */
	Traffic,
	/** 刷新类型，file代表文件刷新，dir代表路径刷新 */
	Type,
	/** 用于获取流的源URL。
	（LiveSrcType为rtmppull/hls时，该字段为必须字段） */
	LifeSrcUrl,
	/** 直播流数（CdnType为live时，该字段为必须字段） */
	LiveStreamCount,
	/** 查询的起始时间，格式为Unix Timestamp。如果有EndTime，BeginTime必须赋值。 */
	BeginTime,
	/** 大文件下载、点播支持文件的md5校验 */
	Md5,
	/** 数据偏移量，默认为0 */
	Offset,
	/** 
	 * 加速成功后需要缓存在节点服务器的静态文件类型，动态文件不支持缓存。
	 * 多个文件类型，请使用：“CacheFileTypes.0=zip, CacheFileTypes.1=txt&”，依赖于CacheTel参数
	 */
	CacheFileTypes,
	/** 直播类型，枚举值为：rtmppush；rtmppull；hls（CdnType为live时，该字段为必须字段） */
	LiveSrcType,
	/** 域名ID，创建加速域名时生成。默认获取账户下所有域名。 */
	DomainIds,
	/** 
	 * 需要缓存的文件或路径的URL。URL支持模糊匹配，不支持正则表达式。<br>
	 * CacheUrls需要以http://Domain/开始。<br>
	 * 如所有jpg文件，即为http://Domian/*.jpg; <br>
	 * 如所有jpg或gif文件，即为http://Domain/*.(jpg|png);<br> 
	 * 如所有a目录下的文件，即为http://Domain/a/*； <br>
	 * 如所有a目录或b目录下的所有jpg文件，即为：http://Domain/(a|b)/*.jpg。<br>
	 * 依赖于CacheTtl参数。
	 */
	CacheUrls;

}
