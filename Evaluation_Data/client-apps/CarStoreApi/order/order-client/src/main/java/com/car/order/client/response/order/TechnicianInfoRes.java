package com.car.order.client.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="TechnicianInfoRes",description="订单技师信息VO")
public class TechnicianInfoRes {


    @ApiModelProperty(value = "技师(代驾)uuid",name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "技师(代驾)姓名",name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "技师(代驾)手机号",name = "technicianMobile")
    private String technicianMobile;

}
