package com.xiaoleilu.ucloud.unet;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 网络 UNet API指令
 * 
 * @author Looly
 *
 */
public enum UNetAction implements Action {
	/** 根据提供信息，分配弹性IP */
	AllocateEIP,
	/** 获取弹性IP详细信息 */
	DescribeEIP,
	/** 修改EIP名字业务组备注等属性字段 */
	UpdateEIPAttribute,
	/** 释放弹性IP资源 */
	ReleaseEIP,
	/** 将弹性IP绑定到资源上 */
	BindEIP,
	/** 将弹性IP从资源上解绑 */
	UnBindEIP,
	/** 修改弹性IP的外网带宽 */
	ModifyEIPBandwidth,
	/** 修改弹性IP的外网出口权重 */
	ModifyEIPWeight,
	/** 获取弹性IP价格 */
	GetEIPPrice,

	/** 根据提供信息，分配内网VIP(Virtual IP，多用于高可用程序作为漂移IP。) */
	AllocateVIP,
	/** 获取内网VIP详细信息 */
	DescribeVIP,
	/** 释放VIP资源 */
	ReleaseVIP,

	/** 获取防火墙组信息 */
	DescribeSecurityGroup,
	/** 获取防火墙组所绑定资源的外网IP */
	DescribeSecurityGroupResource,
	/** 创建防火墙组 */
	CreateSecurityGroup,
	/** 更新防火墙规则 */
	UpdateSecurityGroup,
	/** 将防火墙应用到资源上 */
	GrantSecurityGroup,
	/** 删除防火墙 */
	DeleteSecurityGroup
}
