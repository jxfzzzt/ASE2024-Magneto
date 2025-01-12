package com.xiaoleilu.ucloud.ulb;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * ULB中使用的参数名
 * @author Looly
 *
 */
public enum ULBName implements Name{
	/** 负载均衡的名字 */
	ULBName,
	/** 负载均衡实例的ID */
	ULBId,
	/** 负载均衡名 */
	Name,
	/** 业务 */
	Tag,
	/** 备注 */
	Remark,
	/** VServer实例名称 */
	VServerName,
	/** VServer实例的ID */
	VServerId,
	/** VServer实例的协议，枚举值为： HTTP，TCP，UDP； 默认为HTTP协议 */
	Protocol,
	/** VServer后端端口，取值范围[1-65535]；默认值为80 */
	FrontendPort,
	/** VServer负载均衡模式，枚举值为： Roundrobin，轮询； Source，源地址； 默认为轮询模式 */
	Method,
	/** VServer会话保持方式，枚举值为： None，关闭会话保持； ServerInsert，自动生成； UserDefined，用户自定义； 默认关闭会话保持。 */
	PersistenceType,
	/** 根据PersistenceType确认； None和ServerInsert：此字段无意义； UserDefined：此字段传入自定义会话保持String */
	PersistenceInfo,
	/** 空闲连接的回收时间，单位：秒； 取值范围：(0，86400]，默认值为60 */
	ClientTimeout,
	/** 所添加的后端资源服务端口，取值范围[1-65535]，默认80 */
	Port,
	/** 后端实例状态开关，枚举值： 1：启动； 0：禁用 默认为启用 */
	Enabled,
	/** 所添加的后端资源的类型 */
	ResourceType,
	/** 所添加的后端资源的资源ID */
	ResourceId,
	/** 后端资源实例的ID(ULB后端ID，非资源自身ID) */
	BackendId,
	/** SSL证书的内容 */
	SSLContent,
	/** SSL证书的名字，默认值为空 */
	SSLName,
	/** 所添加的SSL证书类型，目前只支持0：Pem格式 */
	SSLType,
	/** SSL证书的ID */
	SSLId,
	/** 内容转发策略组名称 */
	GroupName,
	/** 内容转发策略组ID */
	GroupId,
	/** 内容转发匹配字段 */
	Match,
	/** 内容转发策略ID */
	PolicyId
}
