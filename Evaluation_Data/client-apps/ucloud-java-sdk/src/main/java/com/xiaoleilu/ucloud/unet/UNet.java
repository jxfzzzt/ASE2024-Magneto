package com.xiaoleilu.ucloud.unet;

import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.core.enums.ResourceType;
import com.xiaoleilu.ucloud.unet.security.SecurityRule;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 网络
 * @author Looly
 *
 */
public class UNet extends Ucloud{
	
	// --------------------------------------------------------------- Constructor start
	/**
	 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
	 */
	public UNet() {
		super();
	}
	/**
	 * 构造
	 * @param config 配置文件
	 */
	public UNet(Config config) {
		super(config);
	}
	/**
	 * 构造
	 * @param client UcloudApiClient
	 */
	public UNet(UcloudApiClient client) {
		super(client);
	}
	// --------------------------------------------------------------- Constructor end
	
	// --------------------------------------------------------------- EIP
	/**
	 * 根据提供信息，分配弹性IP。
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response allocateEIP(Param param){
		return client.get(UNetAction.AllocateEIP, param);
	}
	
	/**
	 * 获取弹性IP详细信息
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeEIP(Param param){
		return client.get(UNetAction.DescribeEIP, param);
	}
	
	/**
	 * 修改弹性IP名字业务组备注等属性字段
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response UpdateEIPAttribute(Param param){
		return client.get(UNetAction.UpdateEIPAttribute, param);
	}
	
	/**
	 * 释放弹性IP资源
	 * @param region 数据中心
	 * @param eipId 弹性IP的资源ID
	 * @return 返回结果
	 */
	public Response releaseEIP(Region region, String eipId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.EIPId, eipId);
		return client.get(UNetAction.ReleaseEIP, param);
	}
	
	/**
	 * 将弹性IP绑定到资源上
	 * @param region 数据中心
	 * @param eipId 弹性IP的资源ID
	 * @param resourceType 弹性IP请求绑定的资源类型
	 * @param resourceId 弹性IP请求绑定的资源ID
	 * @return 返回结果
	 */
	public Response bindEIP(Region region, String eipId, ResourceType resourceType, String resourceId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.EIPId, eipId)
				.set(UNetName.ResourceType, resourceType)
				.set(UNetName.ResourceId, resourceId);
		return client.get(UNetAction.BindEIP, param);
	}
	
	/**
	 * 将弹性IP从资源上解绑
	 * @param region 数据中心
	 * @param eipId 弹性IP的资源ID
	 * @param resourceType 弹性IP请求绑定的资源类型
	 * @param resourceId 弹性IP请求绑定的资源ID
	 * @return 返回结果
	 */
	public Response unBindEIP(Region region, String eipId, ResourceType resourceType, String resourceId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.EIPId, eipId)
				.set(UNetName.ResourceType, resourceType)
				.set(UNetName.ResourceId, resourceId);
		return client.get(UNetAction.UnBindEIP, param);
	}
	
	/**
	 * 修改弹性IP的外网带宽
	 * @param region 数据中心
	 * @param eipId 弹性IP的资源ID
	 * @param bandwidth 弹性IP的外网带宽，单位为Mbps，范围 [0-800]
	 * @return 返回结果
	 */
	public Response modifyEIPBandwidth(Region region, String eipId, int bandwidth){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.EIPId, eipId)
				.set(UNetName.Bandwidth, bandwidth);
		return client.get(UNetAction.ModifyEIPBandwidth, param);
	}
	
	/**
	 * 修改弹性IP的外网出口权重
	 * @param region 数据中心
	 * @param eipId 弹性IP的资源ID
	 * @param weight 外网出口权重，范围[0-100]
	 * @return 返回结果
	 */
	public Response modifyEIPWeight(Region region, String eipId, int weight){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.EIPId, eipId)
				.set(UNetName.Bandwidth, weight);
		return client.get(UNetAction.ModifyEIPWeight, param);
	}
	
	/**
	 * 获取弹性IP价格
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response getEIPPrice(Param param){
		return client.get(UNetAction.GetEIPPrice, param);
	}
	
	// --------------------------------------------------------------- VIP
	/**
	 * 根据提供信息，分配内网VIP(Virtual IP，多用于高可用程序作为漂移IP。)
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response allocateVIP(Param param){
		return client.get(UNetAction.AllocateVIP, param);
	}
	
	/**
	 * 获取内网VIP详细信息
	 * @param region 数据中心
	 * @return 返回结果
	 */
	public Response describeVIP(Region region){
		final Param param = Param.create()
				.set(PubName.Region, region);
		return client.get(UNetAction.DescribeVIP, param);
	}
	
	/**
	 * 获取内网VIP详细信息
	 * @param region 数据中心
	 * @param vip 内网VIP的IP地址
	 * @return 返回结果
	 */
	public Response releaseVIP(Region region, String vip){
		final Param param = Param.create()
				.set(PubName.Region, region);
		return client.get(UNetAction.ReleaseVIP, param);
	}
	
	// --------------------------------------------------------------- 防火墙
	/**
	 * 获取防火墙组信息
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeSecurityGroup(Param param){
		return client.get(UNetAction.DescribeSecurityGroup, param);
	}
	
	/**
	 * 获取防火墙组所绑定资源的外网IP
	 * @param param 参数
	 * @return 返回结果
	 */
	public Response describeSecurityGroupResource(Param param){
		return client.get(UNetAction.DescribeSecurityGroupResource, param);
	}
	
	/**
	 * 创建防火墙组
	 * @param region 数据中心
	 * @param groupName 防火墙组名称
	 * @param description 防火墙组描述
	 * @param rules 规则数组（可配置多个规则）
	 * @return 返回结果
	 */
	public Response createSecurityGroup(Region region, String groupName, String description, SecurityRule... rules){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.GroupName, groupName)
				.set(UNetName.Description, description);
		
		//add rules
		for(int i=0; i < rules.length; i++) {
			param.set("Rule." + i, rules[i]);
		}
		
		return client.get(UNetAction.CreateSecurityGroup, param);
	}
	
	/**
	 * 更新防火墙规则
	 * @param region 数据中心
	 * @param groupId 防火墙资源ID
	 * @param rules 规则数组（可配置多个规则）
	 * @return 返回结果
	 */
	public Response updateSecurityGroup(Region region, String groupId, SecurityRule... rules){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.GroupId, groupId);
		
		//add rules
		for(int i=0; i < rules.length; i++) {
			param.set("Rule." + i, rules[i]);
		}
		
		return client.get(UNetAction.UpdateSecurityGroup, param);
	}
	
	/**
	 * 将防火墙应用到资源上
	 * @param region 数据中心
	 * @param groupId 防火墙资源ID
	 * @param resourceType 所应用资源类型，如UHost
	 * @param resourceId 所应用资源ID
	 * @return 返回结果
	 */
	public Response grantSecurityGroup(Region region, String groupId, ResourceType resourceType, String resourceId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.GroupId, groupId)
				.set(UNetName.ResourceType, resourceType)
				.set(UNetName.ResourceId, resourceId);
		
		return client.get(UNetAction.GrantSecurityGroup, param);
	}
	
	/**
	 * 删除防火墙
	 * @param region 数据中心
	 * @param groupId 防火墙资源ID
	 * @return 返回结果
	 */
	public Response deleteSecurityGroup(Region region, String groupId){
		final Param param = Param.create()
				.set(PubName.Region, region)
				.set(UNetName.GroupId, groupId);
		
		return client.get(UNetAction.DeleteSecurityGroup, param);
	}
}
