package com.car.system.client.request.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/** 
* @author
* @version
* 模块配置
*/
@Data
@ApiModel
public class SettingInfoReq {
	
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称", name = "name", required = true)
	private String name;
	
	/**
	 * 编码
	 */
	@NotBlank(message = "请输入编码！")
	@ApiModelProperty(value = "编码", name = "code", required = true)
	private String code;
	
	/**
	 * 内容
	 */
	@NotBlank(message = "请输入内容！")
	@ApiModelProperty(value = "内容", name = "content", required = true)
	private String content;

}
