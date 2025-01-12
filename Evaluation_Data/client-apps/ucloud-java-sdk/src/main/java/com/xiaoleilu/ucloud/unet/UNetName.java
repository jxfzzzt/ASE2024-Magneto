package com.xiaoleilu.ucloud.unet;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * UnetName中使用的参数名
 * @author Looly
 *
 */
public enum UNetName implements Name{
	/** 弹性IP的线路如下: <br>
	 * 电信: Telecom <br>
	 * 联通: Unicom <br>
	 * 国际: International <br>
	 * BGP: Bgp 双线: Duplet <br>
	 * <br>
	 * 各数据中心允许的线路参数如下： <br>
	 * cn-east-01: Telecom, Unicom, Duplet <br>
	 * cn-south-01: Telecom, Unicom, Duplet <br>
	 * cn-north-01: Bgp cn-north-02: Bgp <br>
	 * cn-north-03: Bgp <br>
	 * hk-01: International <br>
	 * us-west-01: International 
	 */
	OperatorName,
	/**
	 * 弹性IP的外网带宽，单位为Mbps，范围 [0-800]<br>
	 * 共享带宽模式必须指定0M带宽，非共享带宽模式必须指定非0M带宽
	 */
	Bandwidth,
	/** 计费模式，枚举值为： Year，按年付费； Month，按月付费； Dynamic，按需付费(需开启权限)； Trial，试用(需开启权限) 默认为按月付费 */
	ChargeType,
	/** 购买时长，默认: 1 */
	Quantity,
	/** 数据偏移量，默认为0 */
	/** EIP资源ID */
	EIPId,
	/** 弹性IP名 */
	Name,
	/** 弹性IP的业务组标识 */
	Tag,
	/** 弹性IP的备注信息 */
	Remark,
	/** 弹性IP请求绑定的资源类型，枚举值为： uhost：云主机； vrouter：虚拟路由器； ulb，负载均衡器 */
	ResourceType,
	/** 弹性IP请求绑定的资源ID */
	ResourceId,
	/** 申请数量，默认: 1 */
	Count,
	/** 防火墙组名称 */
	GroupName,
	/** 防火墙资源ID */
	GroupId,
	/** 防火墙组描述 */
	Description,
	
	/** 内网VIP的IP地址 */
	VIP,
}
