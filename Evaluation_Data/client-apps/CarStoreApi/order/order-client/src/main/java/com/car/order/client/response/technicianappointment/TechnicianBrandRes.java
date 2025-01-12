package com.car.order.client.response.technicianappointment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianBrandRes {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "技师ID",name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "品牌ID",name = "brandUuid")
    private String brandUuid;

    @ApiModelProperty(value = "维修品牌名称",name = "brandName")
    private String brandName;


}
