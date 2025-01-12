package com.xiaoleilu.ucloud.uhost;

import com.xiaoleilu.ucloud.core.Action;

/**
 * 云主机 UHost API指令
 * @author Looly
 *
 */
public enum UHostAction implements Action{
	/** 指定数据中心，根据资源使用量创建指定数量的UHost实例。 */
	CreateUHostInstance,
	/** 获取主机或主机列表信息，并可根据数据中心，主机ID等参数进行过滤 */
	DescribeUHostInstance,
	/** 删除指定数据中心的UHost实例。 */
	TerminateUHostInstance,
	/**
	 *  修改指定UHost实例的资源配置，如CPU核心数，内存容量大小，磁盘空间大小等。 <br>
	 *  修改配置注意事项：  <br>
	 *  1.修改配置前，请确认该实例已经被关闭。  <br>
	 *  2.修改磁盘空间大小后，请在启动后按照说明，进入操作系统进行操作。 <br>
	 */
	ResizeUHostInstance,
	/**
	 * 重新安装指定UHost实例的操作系统<br>
	 * 1.请确认在重新安装之前，该实例已被关闭； <br>
	 * 2.请确认该实例未挂载UDisk；<br>
	 * 3.将原系统重装为不同类型的系统时(Linux->Windows)，不可选择保留数据盘； <br>
	 * 4.重装不同版本的系统时(CentOS6->CentOS7)，若选择保留数据盘，请注意数据盘的文件系统格式； <br>
	 * 5.若主机CPU低于2核，不可重装为Windows系统。
	 */
	ReinstallUHostInstance,
	/** 启动处于关闭状态的UHost实例，需要指定数据中心及UHostID两个参数的值。 */
	StartUHostInstance,
	/** 指停止处于运行状态的UHost实例，需指定数据中心及UhostID。 */
	StopUHostInstance,
	/** 重新启动UHost实例，需要指定数据中心及UHostID两个参数的值。 */
	RebootUHostInstance,
	/** 重置UHost实例的管理员密码。该操作需要UHost实例处于关闭状态。 */
	ResetUHostInstancePassword,
	/** 修改指定UHost实例名称，需要给出数据中心，UHostId，及新的实例名称。 */
	ModifyUHostInstanceName,
	/** 修改指定UHost实例业务组标识。 */
	ModifyUHostInstanceTag,
	/** 修改指定UHost实例备注信息。 */
	ModifyUHostInstanceRemark,
	/** 根据UHost实例配置，获取UHost实例的价格。 */
	GetUHostInstancePrice,
	/** 获取指定UHost实例的管理VNC配置详细信息。 */
	GetUHostInstanceVncInfo,
	
	/** 获取指定数据中心镜像列表，用户可通过指定操作系统类型，镜像Id进行过滤。 */
	DescribeImage,
	/** 从指定UHost实例，生成自定义镜像。 */
	CreateCustomImage,
	/** 删除用户自定义镜像 */
	TerminateCustomImage,
	
	/** 将一个可用的UDisk挂载到某台主机上，当UDisk挂载成功后，还需要在主机内部进行文件系统操作。 */
	AttachUdisk,
	/** 卸载某个已经挂载在指定UHost实例上的UDisk */
	DetachUdisk,
	
	/** 对指定UHost实例制作数据快照。 */
	CreateUHostInstanceSnapshot,
	/** 获取已经存在的UHost实例的存储快照列表。 */
	DescribeUHostInstanceSnapshot
}
