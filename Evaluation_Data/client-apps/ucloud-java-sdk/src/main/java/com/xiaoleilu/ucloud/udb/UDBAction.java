package com.xiaoleilu.ucloud.udb;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 云数据库 UDB API指令
 * 
 * @author Looly
 *
 */
public enum UDBAction implements Action {
	/** 备份udb实例 */
	BackupUDBInstance,
	/** 清除udb实例的log */
	ClearUDBLog,
	/** 创建udb实例（包括mysql、mongodb实例和从备份恢复实例） */
	CreateUDBInstance,
	/** 从已有配置文件创建新配置文件 */
	CreateUDBParamGroup,
	/** 创建mongodb的副本节点（包括仲裁） */
	CreateUDBReplicationInstance,
	/** 创建udb实例的slave */
	CreateUDBSlave,
	/** 删除udb实例备份 */
	DeleteUDBBackup,
	/** 删除udb实例 */
	DeleteUDBInstance,
	/** 删除配置参数组 */
	DeleteUDBParamGroup,
	/** 获取udb实例的备份黑名单 */
	DescribeUDBBackupBlacklist,
	/** 列表udb实例备份信息 */
	DescribeUDBBackup,
	/** 获取udb实例价格信息 */
	DescribeUDBInstancePrice,
	/** 获取udb实例信息 */
	DescribeUDBInstance,
	/** 获取udb实例状态 */
	DescribeUDBInstanceState,
	/** 获取参数组详细参数信息 */
	DescribeUDBParamGroup,
	/** 获取udb支持的类型信息 */
	DescribeUDBType,
	/** 编辑udb实例的备份黑名单 */
	EditUDBBackupBlacklist,
	/** 重命名udb实例 */
	ModifyUDBInstanceName,
	/** 从库提升为独立库 */
	PromoteUDBSlave,
	/** 重启udb实例 */
	RestartUDBInstance,
	/** 启动udb实例 */
	StartUDBInstance,
	/** 关闭udb实例 */
	StopUDBInstance,
	/** 更新udb配置参数项 */
	UpdateUDBParamGroup,
	/** 导入udb配置 */
	UploadUDBParamGroup

}
