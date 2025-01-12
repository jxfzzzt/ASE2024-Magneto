package com.xiaoleilu.ucloud.uhost;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * Uhost中使用的参数名
 * @author Looly
 *
 */
public enum UHostName implements Name{
	/** 认证方式。密码: Password，key: KeyPair（暂不支持） */
	LoginMode,
	/** UHost密码，LoginMode为Password时此项必须（密码需使用base64进行编码） */
	Password,
	/** Keyname，LoginMode为KeyPair时此项必须（暂不支持） */
	@Deprecated
	KeyPair,
	/** 虚拟CPU核数， 单位：个，范围：[1,16], 最小值为1，其他值是2的倍数， 默认值: 4 */
	CPU,
	/** 内存大小 , 单位：MB 范围[2048,65536]， 步长：2048， 默认值：8192 */
	Memory,
	/** 数据盘大小， 单位：GB， 范围[0,1000]， 步长：10， 默认值：60 */
	DiskSpace,
	/** UHost实例名称， 默认：UHost */
	Name,
	/** 网络Id， 默认：基础网络 */
	NetworkId,
	/** 防火墙Id， 默认：Web防火墙 */
	SecurityGroupId,
	/** 计费模式，枚举值为： Year，按年付费； Month，按月付费； Dynamic，按需付费（需开启权限）； Trial，试用（需开启权限） 默认为月付 */
	ChargeType,
	/** 购买时长，默认: 1 */
	Quantity,
	/** UHost实例ID */
	UHostId,
	/** 业务组标识 */
	Tag,
	/** 备注 */
	Remark,
	/** UDisk实例ID */
	UDiskId,
	/** 购买台数，范围[1,5] */
	Count,
	
	/** 镜像Id, 参见 DescribeImage */
	ImageId,
	/** 操作系统类型：Linux， Windows 默认返回所有类型 */
	OsType,
	/** 标准镜像：Base，行业镜像：Business， 自定义镜像：Custom，默认返回所有类型 */
	ImageType
}
