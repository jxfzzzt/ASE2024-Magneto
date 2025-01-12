package com.car.account.client.request.store;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/16 21:13
 */
@Data
@ApiModel
public class QueryShareStoreListReq {

	@NotNull(message = "请上传经度位置信息")
	@ApiModelProperty(value = "经度", name = "longitude")
	private Float longitude;

	@NotNull(message = "请上传纬度位置信息")
	@ApiModelProperty(value = "纬度", name = "latitude")
	private Float latitude;

	@ApiModelProperty(value = "距离(千米)", name = "distance")
	private Integer distance;

	@ApiModelProperty(value = "汽车品牌主键", name = "brandUuid")
	private String brandUuid;

	@ApiModelProperty(value = "店铺类型主键", name = "storeType")
	private String storeType;

	@ApiModelProperty(value = "共享类型", name = "storeType")
	private String shareStationTypeName;

}
