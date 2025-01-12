package com.xiaoleilu.ucloud.core.enums;

/**
 * 数据中心
 * 请见官方文档：http://docs.ucloud.cn/api/regionlist.html
 * @author Looly
 *
 */
public enum Region {
	/** 北京BGP-A Bgp: BGP线路 */
	CN_NORTH_01("cn-north-01"),
	/** 北京BGP-B Bgp: BGP线路 */
	CN_NORTH_02("cn-north-02"),
	/** 北京BGP-C Bgp: BGP线路 */
	CN_NORTH_03("cn-north-03"),
	
	/** 华东双线 Duplet: 双线, Unicom: 网通, Telecom: 电信 */
	CN_EAST_01("cn-east-01"),
	
	/** 华南双线 Duplet: 双线, Unicom: 网通, Telecom: 电信 */
	CN_SOUTH_01("cn-south-01"),
	
	/** 亚太 International: 国际线路*/
	HK_01("hk-01"),
	
	/** 北美 International: 国际线路*/
	US_WEST_01("us-west-01");
	
	/** 数据中心名 */
	private String regionName;
	
	private Region(String regionName) {
		this.regionName = regionName;
	}
	
	@Override
	public String toString() {
		return this.regionName;
	}
}
