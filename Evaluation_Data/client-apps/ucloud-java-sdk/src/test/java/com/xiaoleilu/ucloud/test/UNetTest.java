package com.xiaoleilu.ucloud.test;

import org.junit.Test;
import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.enums.ChargeType;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.core.enums.ResourceType;
import com.xiaoleilu.ucloud.unet.OperatorName;
import com.xiaoleilu.ucloud.unet.UNet;
import com.xiaoleilu.ucloud.unet.UNetName;
import com.xiaoleilu.ucloud.unet.security.Proto;
import com.xiaoleilu.ucloud.unet.security.SecurityAction;
import com.xiaoleilu.ucloud.unet.security.SecurityRule;

/**
 * 网络测试
 * @author Looly
 *
 */
public class UNetTest {
	private final Logger log = Log.get();
	
	UNet uNet = new UNet();
	
	/**
	 * 第一步：询价
	 */
//	@Test
	public void getEIPPriceTest(){
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(UNetName.OperatorName, OperatorName.Bgp)
				.set(UNetName.Bandwidth, 2)
				.set(UNetName.ChargeType, ChargeType.Month);
		
		Response res = uNet.getEIPPrice(param);
		log.debug("Get EIP Price: {}", res.toPretty());
	}
	
	/**
	 * 第二步：分配弹性IP
	 */
//	@Test
	public void allocateEIPTest(){
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(UNetName.OperatorName, OperatorName.Bgp)
				.set(UNetName.Bandwidth, 2)
				.set(UNetName.ChargeType, ChargeType.Month)
				.set(UNetName.Quantity, 1);
		
		log.debug("Allocate EIP Param: {}", param);
//		Response res = uNet.allocateEIP(param);
//		log.debug("Allocate EIP: {}", res.toPretty());
	}
	
	/**
	 * 第三步：将弹性IP绑定到资源（云主机）上
	 */
//	@Test
	public void bindEIPTest(){
		Response res = uNet.bindEIP(Region.CN_NORTH_03, "eip-kg4hpc", ResourceType.uhost, "uhost-agd0gk");
		log.debug("Bind EIP: {}", res.toPretty());
	}
	
	/**
	 * 第四步：创建防火墙组
	 */
//	@Test
	public void createSecurityGroupTest(){
		SecurityRule tcp8080 = new SecurityRule(Proto.TCP, 8080, "0.0.0.0/0", SecurityAction.ACCEPT, SecurityRule.PRIORITY_MIDDLE);
		SecurityRule tcp22 = new SecurityRule(Proto.TCP, 22, "0.0.0.0/0", SecurityAction.ACCEPT, SecurityRule.PRIORITY_MIDDLE);
		SecurityRule tcp80 = new SecurityRule(Proto.TCP, 80, "0.0.0.0/0", SecurityAction.ACCEPT, SecurityRule.PRIORITY_MIDDLE);
		SecurityRule icmp = new SecurityRule(Proto.ICMP, "0.0.0.0/0", SecurityAction.ACCEPT, SecurityRule.PRIORITY_MIDDLE);
		
		Response res = uNet.createSecurityGroup(Region.CN_NORTH_03, "Java Web", "开放80,8080,22,icmp", tcp8080, tcp22, tcp80, icmp);
		log.debug("JUnit: {}", res.toPretty());
	}
	
	/**
	 * 第五步：获取防火墙组信息
	 */
	@Test
	public void describeSecurityGroupTest(){
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(UNetName.ResourceType, ResourceType.uhost);
		
		Response res = uNet.describeSecurityGroup(param);
		log.debug("Describe Security Group: {}", res.toPretty());
	}
	
	/**
	 * 第六步：将防火墙应用到资源上
	 */
//	@Test
	public void GrantSecurityGroup(){
		Response res = uNet.grantSecurityGroup(Region.CN_NORTH_03, "15589", ResourceType.uhost, "uhost-agd0gk");
		log.debug("JUnit: {}", res.toPretty());
	}
}
