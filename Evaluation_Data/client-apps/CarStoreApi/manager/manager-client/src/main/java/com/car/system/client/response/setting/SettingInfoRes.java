package com.car.system.client.response.setting;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/** 
* @author
* @version
* 类说明 
*/
@Data
@ApiModel
public class SettingInfoRes {
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 编码
	 */
	private String code;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
     * 主键ID
     */
    String uuid;

    /**
     * 是否有效 0:生效 1：无效
     */
    Integer sts;
    /**
     * 创建时间
     */
    String createdTime;
    /**
     * 创建人
     */
    String createdBy;
    /**
     * 更新时间
     */
    String lastUpdatedTime;
    /**
     * 更新人
     */
    String lastUpdatedBy;

}
