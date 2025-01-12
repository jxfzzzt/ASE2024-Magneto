package com.car.order.client.response.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/1/23
 */
@Data
@ApiModel
public class CaseForVehicleItemRes {


    @ApiModelProperty(value = "案例唯一标识",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "案例唯名称",name = "title")
    private String title;

    @ApiModelProperty(value = "技师名称",name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "案例金额",name = "amt")
    private BigDecimal amt;

    @ApiModelProperty(value = "工龄",name = "workingYear")
    private Integer workingYear;

    @ApiModelProperty(value = "案例图片",name = "imgUrl")
    private String imgUrl;
}
