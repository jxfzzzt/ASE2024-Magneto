package com.car.order.client.request.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 发起咨询
 * @author zhouz
 * @date 2021/1/2
 */
@Data
@ApiModel(value="AddConsultReq",description="新增咨询订单请求VO对象")
public class AddConsultReq {

    @ApiModelProperty(value = "技师uuid",name = "technicianUuid")
    private String technicianUuid;

    @NotBlank(message = "请输入咨询标题！")
    @ApiModelProperty(value = "咨询标题",name = "consultTitle")
    private String consultTitle;

    @NotBlank(message = "请输入咨询内容！")
    @ApiModelProperty(value = "咨询内容",name = "consultContent")
    private String consultContent;

    @ApiModelProperty(value = "咨询内容相关图片",name = "consultImgList")
    private List<String> consultImgList;
    
    @ApiModelProperty(value = "车辆品牌",name = "vehicleBrand")
    private String vehicleBrand;

    @ApiModelProperty(value = "车型类型",name = "vehicleModel")
    private String vehicleModel;
}
