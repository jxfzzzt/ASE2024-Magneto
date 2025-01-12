package com.car.account.client.request.vehicle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-09 09:37
 */
@Data
@ApiModel
public class VehicleTestingDetailReq {

    @NotBlank(message = "类型！")
    @ApiModelProperty("类型")
    private String type ;

    @NotBlank(message = "请输入问题！")
    @ApiModelProperty("问题")
    private String title ;

    @NotNull(message = "0正常1异常")
    @ApiModelProperty("0正常1异常")
    private Boolean yesNo ;
}