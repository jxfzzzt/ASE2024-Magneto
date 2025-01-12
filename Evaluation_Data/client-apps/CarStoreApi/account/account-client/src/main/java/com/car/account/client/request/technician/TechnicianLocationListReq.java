package com.car.account.client.request.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author zhoujian
 */
@Data
@ApiModel(value = "TechnicianLocationListReq", description = "查询布点技师信息列表入参")
public class TechnicianLocationListReq {

    @ApiModelProperty(value = "中心点经度")
    private Float longitude;

    @ApiModelProperty(value = "中心点纬度")
    private Float latitude;

    @ApiModelProperty(value = "获取范围（默认100公里）")
    private Integer zoomNum = 100;
    

	@ApiModelProperty(value = "距离(千米)", name = "distance")
	private Integer distance;

	@ApiModelProperty(value = "汽车品牌主键", name = "brandUuid")
	private String brandUuid;

	@ApiModelProperty(value = "技术类型", name = "technologyType")
    private String technologyType;
}
