package com.car.system.client.request.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/** 
* @author
* @version
* 类说明 
*/
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class SettingInfoUpdateReq extends SettingInfoReq {

	/**
	 * 主键
	 */
	@NotBlank(message = "请输入uuid！")
	@ApiModelProperty(value="主键",name="uuid")
    String uuid;
	
}
