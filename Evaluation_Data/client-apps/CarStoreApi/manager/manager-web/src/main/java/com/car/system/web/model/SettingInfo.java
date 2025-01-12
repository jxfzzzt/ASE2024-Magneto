package com.car.system.web.model;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/** 
* @author
* @version
* 模块配置
*/
@Data
@Table(name = "info_setting")
@EqualsAndHashCode(callSuper = true)
public class SettingInfo extends BaseModelInfo {

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * 编码
	 */
	@Column(name = "code")
	private String code;
	
	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;
}
