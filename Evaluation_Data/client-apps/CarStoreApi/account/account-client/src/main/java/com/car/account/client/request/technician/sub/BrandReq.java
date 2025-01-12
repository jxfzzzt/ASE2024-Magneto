package com.car.account.client.request.technician.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/19 23:47
 */
@Data
@ApiModel
public class BrandReq {

    @ApiModelProperty("品牌uuid")
    private String brandUuid;

    @ApiModelProperty("品牌名字")
    private String brandName;
}
