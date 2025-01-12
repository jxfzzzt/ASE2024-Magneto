package com.xiaoleilu.ucloud.udb;

import com.xiaoleilu.ucloud.core.Param.Name;

/**
 * 云数据库UDB中使用的参数名
 * 
 * @author Looly
 *
 */
public enum UDBName implements Name {

	/** 配置参数组名称 */
	GroupName,
	/** 实例的新名字 */
	Name,
	/** DB实例使用的配置参数组id */
	ParamGroupId,
	/** 参数组id */
	GroupId,
	/** DB实例的id */
	DBId,
	/** 删除时间点(至少前一天)之前log，采用时间戳(秒)，默认当 前时间点前一天 */
	BeforeTime,
	/** 是否锁主库，默认为true */
	IsLock,
	/** 数据中心，请参见数据中心RegionList */
	Region,
	/** 内存限制(MB)，目前支持以下几档 600M/1500M/3000M/6000M/15000M/30000M */
	MemoryLimit,
	/** 购买时长，默认值1 */
	Quantity,
	/**
	 * 配置内容，导入的配置内容采用base64编码。 <br>
	 * mysql只支持[mysqld]段，如： <br>
	 * [mysqld] back_log=102 character_set_server=utf8 ...... mongodb则不需要带段，<br>
	 * 如： auth=true maxConns=2000 ......<br>
	 */
	Content,
	/** 参数值 */
	Value,
	/** Year， Month， Dynamic，Trial，默认: Dynamic 如果不指定，则一次性获取三种计费 */
	ChargeType,
	/** 黑名单，规范示例abc.%;user.%;city.address; */
	Blacklist,
	/** 端口号，mysql默认3306，mongodb默认27017 */
	Port,
	/** 日志类型，10-error（暂不支持）、20-slow（暂不支持 ）、30-binlog */
	LogType,
	/** 是否使用SSD，默认为false */
	UseSSD,
	/** 购买DB实例数量 */
	Count,
	/** 源参数组id */
	SrcGroupId,
	/** master实例的DBId */
	SrcId,
	/** DB种类，分为SQL和NOSQL，如果是别表操作，则需要制定 */
	ClassType,
	/** 管理员帐户名，默认root */
	AdminUser,
	/** 是否是仲裁节点，默认false，仲裁节点按最小机型创建 */
	IsArbiter,
	/** 参数名称 */
	Key,
	/** 项目编号 */
	ProjectId,
	/** 是否强制(如果从库落后可能会禁止提升)，默认false 如果落后情况下，强制提升丢失数据 */
	IsForce,
	/** 备份策略，备份时间间隔，单位小时计，默认24小时 */
	BackupDuration,
	/** 是否使用黑名单备份，默认false */
	UseBlacklist,
	/** 磁盘空间(GB), 暂时支持20G - 500G */
	DiskSpace,
	/**
	 * DB类型id，mysql/mongodb按版本细分各有一个id <br>
	 * 1：mysql-5.5<br>
	 * 2：mysql-5.1<br>
	 * 3：percona-5.5 <br>
	 * 4：mongodb-2.4<br>
	 * 5：mongodb-2.6<br>
	 * 6：mysql-5.6<br>
	 * 7：percona-5.6<br>
	 */
	DBTypeId,
	/** 备份策略，每周备份数量，默认7次 */
	BackupCount,
	/** 参数组描述 */
	Description,
	/** 备份名称 */
	BackupName,
	/** 过滤条件:结束时间(时间戳) */
	EndTime,
	/** 备份策略，备份开始时间，单位小时计，默认3点 */
	BackupTime,
	/** 使用的代金券id */
	CouponId,
	/** 备份类型，包括0-自动，1-手动 */
	BackupType,
	/** 过滤条件:起始时间(时间戳) */
	BeginTime,
	/** 管理员密码 */
	AdminPassword
}