package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value="QueryShareStoreListRes")
public class QueryShareStoreListRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "可共享车位",name = "shareCount")
    private Integer shareCount;

    @ApiModelProperty(value = "图片地址",name = "imageUrl")
    private String imageUrl;

    @ApiModelProperty(value = "品牌",name = "brandName")
    private String brandName;

    @ApiModelProperty(value = "纬度",name = "latitude")
    private Float latitude;

    @ApiModelProperty(value = "经度",name = "longitude")
    private Float longitude;
}
