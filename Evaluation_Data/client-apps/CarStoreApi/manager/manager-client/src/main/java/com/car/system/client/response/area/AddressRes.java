package com.car.system.client.response.area;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-09 19:33
 */
@Data
public  class AddressRes {
    @ApiModelProperty("国家")
    private String country;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区编码")
    private String adcode;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("区")
    private String district;

}