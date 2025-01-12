package com.car.account.client.request.position;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhangyp
 * @date 2021/1/16 21:13
 */
@Data
@ApiModel
public class PositionReq {

    @NotNull(message = "请上传经度位置信息")
    @ApiModelProperty(value = "经度",name = "longitude")
    private Float longitude;

    @NotNull(message = "请上传纬度位置信息")
    @ApiModelProperty(value = "纬度",name = "latitude")
    private Float latitude;
}
