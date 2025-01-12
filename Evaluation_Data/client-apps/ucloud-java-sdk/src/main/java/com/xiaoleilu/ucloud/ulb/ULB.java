package com.xiaoleilu.ucloud.ulb;

import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.Ucloud;
import com.xiaoleilu.ucloud.core.UcloudApiClient;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.util.Config;

/**
 * 负载均衡
 * @author Looly
 *
 */
public class ULB extends Ucloud{
	
	// --------------------------------------------------------------- Constructor start
		/**
		 * 构造，公钥、私钥、API的URL读取默认配置文件中的信息
		 */
		public ULB() {
			super();
		}
		/**
		 * 构造
		 * @param config 配置文件
		 */
		public ULB(Config config) {
			super(config);
		}
		/**
		 * 构造
		 * @param client UcloudApiClient
		 */
		public ULB(UcloudApiClient client) {
			super(client);
		}
		// --------------------------------------------------------------- Constructor end
		
		/**
		 * 创建负载均衡实例
		 * @param region 数据中心
		 * @param ulbName 负载均衡的名字
		 * @return 返回结果
		 */
		public Response createULB(Region region, String ulbName){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.ULBName, ulbName);
			return client.get(ULBAction.CreateULB, param);
		}
		
		/**
		 * 删除负载均衡实例
		 * @param region 数据中心
		 * @param ulbId 负载均衡的ID
		 * @return 返回结果
		 */
		public Response deleteULB(Region region, String ulbId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.ULBId, ulbId);
			return client.get(ULBAction.DeleteULB, param);
		}
		
		/**
		 * 获取ULB详细信息
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response describeULB(Param param){
			return client.get(ULBAction.DescribeULB, param);
		}
		
		/**
		 * 修改ULB名字业务组备注等属性字段
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response updateULBAttribute(Param param){
			return client.get(ULBAction.UpdateULBAttribute, param);
		}
		
		/**
		 * 创建VServer实例
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response createVServer(Param param){
			return client.get(ULBAction.CreateVServer, param);
		}
		
		/**
		 * 删除VServer实例
		 * @param region 数据中心
		 * @param ulbId 负载均衡的ID
		 * @param vServerId VServer实例的ID
		 * @return 返回结果
		 */
		public Response deleteVServer(Region region, String ulbId, String vServerId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.ULBId, ulbId)
					.set(ULBName.VServerId, vServerId);
			return client.get(ULBAction.DeleteVServer, param);
		}
		
		/**
		 * 修改VServer实例属性
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response UpdateVServerAttribute(Param param){
			return client.get(ULBAction.UpdateVServerAttribute, param);
		}
		
		/**
		 * 修改VServer实例属性
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response allocateBackend(Param param){
			return client.get(ULBAction.AllocateBackend, param);
		}
		
		/**
		 * 释放ULB后端资源实例
		 * @param region 数据中心
		 * @param ulbId 负载均衡的ID
		 * @param backendId 后端资源实例的ID(ULB后端ID，非资源自身ID)
		 * @return 返回结果
		 */
		public Response releaseBackend(Region region, String ulbId, String backendId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.ULBId, ulbId)
					.set(ULBName.BackendId, backendId);
			return client.get(ULBAction.ReleaseBackend, param);
		}
		
		/**
		 * 修改ULB后端资源实例(主机池)属性
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response updateBackendAttribute(Param param){
			return client.get(ULBAction.UpdateBackendAttribute, param);
		}
		
		/**
		 * 添加SSL证书
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response createSSL(Param param){
			return client.get(ULBAction.CreateSSL, param);
		}
		
		/**
		 * 删除SSL证书
		 * @param region 数据中心
		 * @param sslId SSL证书的ID
		 * @return 返回结果
		 */
		public Response deleteSSL(Region region, String sslId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.SSLId, sslId);
			return client.get(ULBAction.DeleteSSL, param);
		}
		
		/**
		 * 将SSL证书绑定到VServer
		 * @param region 数据中心
		 * @param sslId SSL证书的ID
		 * @return 返回结果
		 */
		public Response bindSSL(Region region, String sslId, String ulbId, String vServerId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.SSLId, sslId)
					.set(ULBName.ULBId, ulbId)
					.set(ULBName.VServerId, vServerId);
			return client.get(ULBAction.BindSSL, param);
		}
		
		/**
		 * 显示SSL证书信息
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response describeSSL(Param param){
			return client.get(ULBAction.DescribeSSL, param);
		}
		
		/**
		 * 创建内容转发策略组
		 * @param region 数据中心
		 * @param groupName 内容转发策略组名称，默认为空
		 * @return 返回结果
		 */
		public Response createPolicyGroup(Region region, String groupName){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.ULBName, groupName);
			return client.get(ULBAction.CreatePolicyGroup, param);
		}
		
		/**
		 * 删除内容转发策略组
		 * @param region 数据中心
		 * @param groupId 内容转发策略组ID
		 * @return 返回结果
		 */
		public Response deletePolicyGroup(Region region, String groupId){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.GroupId, groupId);
			return client.get(ULBAction.DeletePolicyGroup, param);
		}
		
		/**
		 * 显示内容转发策略组详情
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response describePolicyGroup(Param param){
			return client.get(ULBAction.DescribePolicyGroup, param);
		}
		
		/**
		 * 修改内容转发策略组配置信息
		 * @param region 数据中心
		 * @param groupId 内容转发策略组ID
		 * @param groupName 修改策略转发组名称
		 * @return 返回结果
		 */
		public Response updatePolicyGroupAttribute(Region region, String groupId, String groupName){
			final Param param = Param.create()
					.set(PubName.Region, region)
					.set(ULBName.GroupId, groupId)
					.set(ULBName.GroupName, groupName);
			return client.get(ULBAction.UpdatePolicyGroupAttribute, param);
		}
		
		/**
		 * 创建内容转发策略
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response createPolicy(Param param){
			return client.get(ULBAction.CreatePolicy, param);
		}
		
		/**
		 * 删除内容转发策略
		 * @param param 参数
		 * @return 返回结果
		 */
		public Response deletePolicy(Param param){
			return client.get(ULBAction.DeletePolicy, param);
		}
}
