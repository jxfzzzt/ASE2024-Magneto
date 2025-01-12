package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel
public class QueryAreaStoreListRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

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

    @ApiModelProperty(value = "公司地址-县镇",name = "companyAddressCounty")
    private String companyAddressCounty;

    @ApiModelProperty(value = "县镇名称",name = "addressCountyName")
    private String addressCountyName;

    @ApiModelProperty(value = "详细地址",name = "companyAddressDetail")
    private String companyAddressDetail;


}
