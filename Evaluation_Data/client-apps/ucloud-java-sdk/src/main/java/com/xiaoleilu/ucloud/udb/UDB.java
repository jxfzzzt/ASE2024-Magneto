package com.xiaoleilu.ucloud.udb;

import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 云数据库
 * @author Looly
 *
 */
public class UDB extends Ucloud {

	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UDB() {
		super();
	}

	/**
	 * 构造
	 * 
	 * @param config 配置文件
	 */
	public UDB(Config config) {
		super(config);
	}

	/**
	 * 构造
	 * 
	 * @param client UcloudApiClient
	 */
	public UDB(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end

	/**
	 * 备份udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response backupUDBInstance(Param param) {
		return client.get(UDBAction.BackupUDBInstance, param);
	}

	/**
	 * 清除udb实例的log
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response clearUDBLog(Param param) {
		return client.get(UDBAction.ClearUDBLog, param);
	}

	/**
	 * 创建udb实例（包括mysql、mongodb实例和从备份恢复实例）
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUDBInstance(Param param) {
		return client.get(UDBAction.CreateUDBInstance, param);
	}

	/**
	 * 从已有配置文件创建新配置文件
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUDBParamGroup(Param param) {
		return client.get(UDBAction.CreateUDBParamGroup, param);
	}

	/**
	 * 创建mongodb的副本节点（包括仲裁）
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUDBReplicationInstance(Param param) {
		return client.get(UDBAction.CreateUDBReplicationInstance, param);
	}

	/**
	 * 创建udb实例的slave
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUDBSlave(Param param) {
		return client.get(UDBAction.CreateUDBSlave, param);
	}

	/**
	 * 删除udb实例备份
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response deleteUDBBackup(Param param) {
		return client.get(UDBAction.DeleteUDBBackup, param);
	}

	/**
	 * 删除udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response deleteUDBInstance(Param param) {
		return client.get(UDBAction.DeleteUDBInstance, param);
	}

	/**
	 * 删除配置参数组
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response deleteUDBParamGroup(Param param) {
		return client.get(UDBAction.DeleteUDBParamGroup, param);
	}

	/**
	 * 获取udb实例的备份黑名单
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBBackupBlacklist(Param param) {
		return client.get(UDBAction.DescribeUDBBackupBlacklist, param);
	}

	/**
	 * 列表udb实例备份信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBBackup(Param param) {
		return client.get(UDBAction.DescribeUDBBackup, param);
	}

	/**
	 * 获取udb实例价格信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBInstancePrice(Param param) {
		return client.get(UDBAction.DescribeUDBInstancePrice, param);
	}

	/**
	 * 获取udb实例信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBInstance(Param param) {
		return client.get(UDBAction.DescribeUDBInstance, param);
	}

	/**
	 * 获取udb实例状态
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBInstanceState(Param param) {
		return client.get(UDBAction.DescribeUDBInstanceState, param);
	}

	/**
	 * 获取参数组详细参数信息
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUDBParamGroup(Param param) {
		return client.get(UDBAction.DescribeUDBParamGroup, param);
	}

	/**
	 * 获取udb支持的类型信息
	 * 
	 * @param region 数据中心
	 * @return 返回结果
	 */
	public Response describeUDBType(Region region) {
		return client.get(UDBAction.DescribeUDBType, Param.create().set(PubName.Region, region));
	}

	/**
	 * 编辑udb实例的备份黑名单
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response editUDBBackupBlacklist(Param param) {
		return client.get(UDBAction.EditUDBBackupBlacklist, param);
	}

	/**
	 * 重命名udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response modifyUDBInstanceName(Param param) {
		return client.get(UDBAction.ModifyUDBInstanceName, param);
	}

	/**
	 * 从库提升为独立库
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response promoteUDBSlave(Param param) {
		return client.get(UDBAction.PromoteUDBSlave, param);
	}

	/**
	 * 重启udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response restartUDBInstance(Param param) {
		return client.get(UDBAction.RestartUDBInstance, param);
	}

	/**
	 * 启动udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response startUDBInstance(Param param) {
		return client.get(UDBAction.StartUDBInstance, param);
	}

	/**
	 * 关闭udb实例
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response stopUDBInstance(Param param) {
		return client.get(UDBAction.StopUDBInstance, param);
	}

	/**
	 * 更新udb配置参数项
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response updateUDBParamGroup(Param param) {
		return client.get(UDBAction.UpdateUDBParamGroup, param);
	}

	/**
	 * 导入udb配置
	 * 
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response uploadUDBParamGroup(Param param) {
		return client.get(UDBAction.UploadUDBParamGroup, param);
	}

}
