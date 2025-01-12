package com.car.system.client.request.setting;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
* @author
* @version
* 类说明 
*/
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class SettingInfoListReq extends PageReq {

	
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称", name = "name")
	private String name;
	
	/**
	 * 编码
	 */
	@ApiModelProperty(value = "编码", name = "code")
	private String code;
	
	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容", name = "content")
	private String content;
}
