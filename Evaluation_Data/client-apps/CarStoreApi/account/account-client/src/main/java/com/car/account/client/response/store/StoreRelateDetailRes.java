package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2021/1/31
 */
@Data
@ApiModel
public class StoreRelateDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "storeUuid",name = "店铺uuid")
    private String storeUuid;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺类型,对应字典表 uuid",name = "storeType")
    private String storeType;

    @ApiModelProperty(value = "店铺类型名称",name = "storeTypeName")
    private String storeTypeName;

    @ApiModelProperty(value = "省份",name = "公司地址-省,对应地区表uuid")
    private String companyAddressProvince;

    @ApiModelProperty(value = "省份名称",name = "addressProvinceName")
    private String addressProvinceName;

    @ApiModelProperty(value = "城市名称",name = "addressCityName")
    private String addressCityName;

    @ApiModelProperty(value = "城市",name = "companyAddressCity")
    private String companyAddressCity;

    @ApiModelProperty(value = "详细地址",name = "companyAddressDetail")
    private String companyAddressDetail;

}
