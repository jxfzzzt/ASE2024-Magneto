package com.car.account.client.response.store.sub;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/17 14:05
 */
@Data
@ApiModel
public class ImgStoreRes {

    @ApiModelProperty("类型,1营业执照 ,2门店图片")
    private Integer imageType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("商铺uuid")
    private String storeUuid;
}
