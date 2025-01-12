package com.xiaoleilu.ucloud.uhost;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.uhost.image.Image;
import com.xiaoleilu.ucloud.uhost.image.ImageFilter;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 云主机
 * @author Looly
 *
 */
public class UHost extends Ucloud{
	
	public final static String NAME_IMAGE_SET = "ImageSet";
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UHost() {
		super();
	}
	/**
	 * 构造
	 * @param config 配置文件
	 */
	public UHost(Config config) {
		super(config);
	}
	/**
	 * 构造
	 * @param client UcloudApiClient
	 */
	public UHost(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end
	
	/**
	 * 指定数据中心，根据资源使用量创建指定数量的UHost实例。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createUHostInstance(Param param){
		return client.get(UHostAction.CreateUHostInstance, param);
	}
	
	/**
	 * 获取主机或主机列表信息，并可根据数据中心，主机ID等参数进行过滤。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeUHostInstance(Param param){
		return client.get(UHostAction.DescribeUHostInstance, param);
	}
	
	/**
	 * 删除指定数据中心的UHost实例。
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response terminateUHostInstance(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.DescribeUHostInstance, param);
	}
	
	/**
	 *  修改指定UHost实例的资源配置，如CPU核心数，内存容量大小，磁盘空间大小等。 <br>
	 *  修改配置注意事项：  <br>
	 *  1.修改配置前，请确认该实例已经被关闭。  <br>
	 *  2.修改磁盘空间大小后，请在启动后按照说明，进入操作系统进行操作。 <br>
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response resizeUHostInstance(Param param){
		return client.get(UHostAction.ResizeUHostInstance, param);
	}
	
	/**
	 * 重新安装指定UHost实例的操作系统<br>
	 * 1.请确认在重新安装之前，该实例已被关闭； <br>
	 * 2.请确认该实例未挂载UDisk；<br>
	 * 3.将原系统重装为不同类型的系统时(Linux->Windows)，不可选择保留数据盘； <br>
	 * 4.重装不同版本的系统时(CentOS6->CentOS7)，若选择保留数据盘，请注意数据盘的文件系统格式； <br>
	 * 5.若主机CPU低于2核，不可重装为Windows系统。<br>
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response reinstallUHostInstance(Param param){
		return client.get(UHostAction.ReinstallUHostInstance, param);
	}
	
	/**
	 * 启动处于关闭状态的UHost实例
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response startUHostInstance(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.StartUHostInstance, param);
	}
	
	/**
	 * 停止处于运行状态的UHost实例
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response stopUHostInstance(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.StopUHostInstance, param);
	}
	
	/**
	 * 重新启动UHost实例
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response rebootUHostInstance(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.RebootUHostInstance, param);
	}
	
	/**
	 * 重置UHost实例的管理员密码
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param password 新密码
	 * @return 返回结果
	 */
	public Response resetUHostInstancePassword(Region region, String uHostId, String password){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.setPassword(password);
		return client.get(UHostAction.ResetUHostInstancePassword, param);
	}
	
	/**
	 * 修改指定UHost实例名称，需要给出数据中心，UHostId，及新的实例名称
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param name 新的实例名称
	 * @return 返回结果
	 */
	public Response modifyUHostInstanceName(Region region, String uHostId, String name){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.set(UHostName.Name, name);
		return client.get(UHostAction.ModifyUHostInstanceName, param);
	}
	
	/**
	 * 修改指定UHost实例业务组标识
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param tag 业务组标识
	 * @return 返回结果
	 */
	public Response modifyUHostInstanceTag(Region region, String uHostId, String tag){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.set(UHostName.Tag, tag);
		return client.get(UHostAction.ModifyUHostInstanceTag, param);
	}
	
	/**
	 * 修改指定UHost实例备注信息
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param remark 备注信息
	 * @return 返回结果
	 */
	public Response modifyUHostInstanceRemark(Region region, String uHostId, String remark){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.set(UHostName.Remark, remark);
		return client.get(UHostAction.ModifyUHostInstanceRemark, param);
	}
	
	/**
	 * 根据UHost实例配置，获取UHost实例的价格。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getUHostInstancePrice(Param param){
		return client.get(UHostAction.GetUHostInstancePrice, param);
	}
	
	/**
	 * 获取指定UHost实例的管理VNC配置详细信息。
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response getUHostInstanceVncInfo(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.GetUHostInstanceVncInfo, param);
	}
	
	/**
	 * 获取主机或主机列表信息，并可根据数据中心，主机ID等参数进行过滤。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeImage(Param param){
		return describeImage(param, null);
	}
	
	/**
	 * 获取主机或主机列表信息，并可根据数据中心，主机ID等参数进行过滤。
	 * @param param 参数
	 * @param filter 镜像过滤器
	 * @return 返回结果
	 */
	public Response describeImage(Param param, ImageFilter filter){
		final Response resp = client.get(UHostAction.DescribeImage, param);
		final JSONArray imageSet = resp.getJson().getJSONArray(NAME_IMAGE_SET);
		
		//过滤
		if(null != filter) {
			final JSONArray filteredImageSet = new JSONArray();
			JSONObject imageJson;
			for(int i = 0; i < imageSet.size(); i++) {
				imageJson = imageSet.getJSONObject(i);
				if(filter.filter(Image.parse(imageJson))) {
					filteredImageSet.add(imageJson);
				}
			}
			resp.getJson().put(NAME_IMAGE_SET, filteredImageSet);
		}
		
		return resp;
	}
	
	/**
	 * 获取主机或主机列表信息，并可根据数据中心，主机ID等参数进行过滤。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response createCustomImage(Param param){
		return client.get(UHostAction.CreateCustomImage, param);
	}
	
	/**
	 * 删除用户自定义镜像
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response terminateCustomImage(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.TerminateCustomImage, param);
	}
	
	/**
	 * 将一个可用的UDisk挂载到某台主机上，当UDisk挂载成功后，还需要在主机内部进行文件系统操作
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param uDiskId 需要挂载的UDisk实例ID
	 * @return 返回结果
	 */
	public Response attachUdisk(Region region, String uHostId, String uDiskId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.set(UHostName.UDiskId, uDiskId);
		return client.get(UHostAction.AttachUdisk, param);
	}
	
	/**
	 * 卸载某个已经挂载在指定UHost实例上的UDisk
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @param uDiskId 需要挂载的UDisk实例ID
	 * @return 返回结果
	 */
	public Response detachUdisk(Region region, String uHostId, String uDiskId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId)
				.set(UHostName.UDiskId, uDiskId);
		return client.get(UHostAction.DetachUdisk, param);
	}
	
	/**
	 * 对指定UHost实例制作数据快照
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response createUHostInstanceSnapshot(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.CreateUHostInstanceSnapshot, param);
	}
	
	/**
	 * 获取已经存在的UHost实例的存储快照列表
	 * @param region 数据中心
	 * @param uHostId UHost资源Id
	 * @return 返回结果
	 */
	public Response describeUHostInstanceSnapshot(Region region, String uHostId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UHostName.UHostId, uHostId);
		return client.get(UHostAction.DescribeUHostInstanceSnapshot, param);
	}
}
