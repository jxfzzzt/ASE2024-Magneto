package com.xiaoleilu.ucloud.test;

import org.junit.Test;
import org.slf4j.Logger;

import com.xiaoleilu.hutool.Log;
import com.xiaoleilu.ucloud.core.Param;
import com.xiaoleilu.ucloud.core.Response;
import com.xiaoleilu.ucloud.core.enums.ChargeType;
import com.xiaoleilu.ucloud.core.enums.PubName;
import com.xiaoleilu.ucloud.core.enums.Region;
import com.xiaoleilu.ucloud.uhost.LoginMode;
import com.xiaoleilu.ucloud.uhost.UHostName;
import com.xiaoleilu.ucloud.uhost.UHost;
import com.xiaoleilu.ucloud.uhost.image.Image;
import com.xiaoleilu.ucloud.uhost.image.ImageFilter;
import com.xiaoleilu.ucloud.uhost.image.ImageType;
import com.xiaoleilu.ucloud.uhost.image.OsType;

/**
 * 云主机单元测试类
 * @author Looly
 *
 */
public class UHostTest {
	private final static  Logger log = Log.get();
	
	private final UHost uhost = new UHost();
	
	/**
	 * 第一步：查找我所需的镜像ID
	 */
	@Test
	public void describeImage(){
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(UHostName.OsType, OsType.Linux)
				.set(UHostName.ImageType, ImageType.Base)
				.set(PubName.Offset, 0)
				.set(PubName.Limit, 5);
		
		//镜像过滤器，官方API没有提供镜像的筛选功能，在此做了一个简易的镜像筛选。
		//filter方法就是筛选镜像的，满足条件的镜像返回true，在此我筛选出操作系统名称包含"centos 7"关键字的镜像
		ImageFilter imageFilter = new ImageFilter(){
			
			@Override
			public boolean filter(Image image) {
				return image.getOsName().toLowerCase().contains("centos 7");
			}
		};
		
		Response resp = uhost.describeImage(param, imageFilter);
		log.debug("Describe Image: {}", resp.toPretty());
	}
	
	/**
	 * 第二步：询价
	 */
	@Test
	public void getUhostInstacePrice(){
		final Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				//CentOS 7.0 64位
				.set(UHostName.ImageId, "uimage-5yt2b0")
				.set(UHostName.CPU, 1)
				.set(UHostName.Memory, 2048)
				.set(UHostName.Count, 1)
				.set(UHostName.DiskSpace, 10)
				.set(UHostName.ChargeType, ChargeType.Month);
		
		Response resp = uhost.getUHostInstancePrice(param);
		log.debug("UHost Instance Price: {}", resp.toPretty());
	}
	
	/**
	 * 第三步：创建云主机
	 */
//	@Test
	public void createUHostInstance(){
		final Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				//CentOS 7.0 64位
				.set(UHostName.ImageId, "uimage-5yt2b0")
				.set(UHostName.LoginMode, LoginMode.Password)
				.setPassword("123456")
				.set(UHostName.CPU, 1)
				.set(UHostName.Memory, 2048)
				.set(UHostName.DiskSpace, 10)
				.set(UHostName.Name, "LoolyCentOS7")
				.set(UHostName.ChargeType, ChargeType.Month)
				.set(UHostName.Quantity, 1);
		
		Response resp = uhost.createUHostInstance(param);
		log.debug("Create Instance: {}", resp.toPretty());
	}
	
	/**
	 * 第四步：查看创建后的云主机信息
	 */
	@Test
	public void describeUHostInstance(){
		Param param = Param.create()
				.set(PubName.Region, Region.CN_NORTH_03)
				.set(PubName.Offset, 0)
				.set(PubName.Limit, 50);
		
		Response resp = uhost.describeUHostInstance(param);
		log.debug("Describe UHost Instance: {}", resp.toPretty());
	}
	
	/**
	 * 第五步：关闭云主机
	 */
//	@Test
	public void stopHostInstance(){
		Response resp = uhost.stopUHostInstance(Region.CN_NORTH_03, "uhost-agd0gk");
		log.debug("Stop UHost Instance: {}", resp.toPretty());
	}
	
	/**
	 * 第六步：启动云主机
	 */
//	@Test
	public void startHostInstance(){
		Response resp = uhost.startUHostInstance(Region.CN_NORTH_03, "uhost-agd0gk");
		log.debug("Start UHost Instance: {}", resp.toPretty());
	}
}
