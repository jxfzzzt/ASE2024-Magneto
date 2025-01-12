package com.car.account.client.response.position;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/16 21:13
 */
@Data
@ApiModel
public class PositionRes {

	@ApiModelProperty(value = "经度", name = "longitude")
	private Float longitude;

	@ApiModelProperty(value = "纬度", name = "latitude")
	private Float latitude;
}
