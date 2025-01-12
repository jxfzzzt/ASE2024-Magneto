package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value="StoreDetailRes",description="店铺详情VO")
public class StoreDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺类型,对应字典表 uuid",name = "storeType")
    private String storeType;

    @ApiModelProperty(value = "公司名称",name = "companyName")
    private String companyName;

    @ApiModelProperty(value = "公司地址-省",name = "companyAddressProvince")
    private String companyAddressProvince;

    @ApiModelProperty(value = "省份名称",name = "addressProvinceName")
    private String addressProvinceName;

    @ApiModelProperty(value = "公司地址-市",name = "companyAddressCity")
    private String companyAddressCity;

    @ApiModelProperty(value = "城市名称",name = "addressCityName")
    private String addressCityName;

    @ApiModelProperty(value = "公司地址-县镇",name = "companyAddressCounty")
    private String companyAddressCounty;

    @ApiModelProperty(value = "县镇名称",name = "addressCountyName")
    private String addressCountyName;

    @ApiModelProperty(value = "公司地址-详细信息",name = "companyAddressDetail")
    private String companyAddressDetail;

    @ApiModelProperty(value = "汽车品牌列表 uuid",name = "brandUuidList")
    private List<String> brandUuidList;

    @ApiModelProperty(value = "营业执照图片",name = "businessImgList")
    private List<String> businessImgList;

    @ApiModelProperty(value = "门店图片",name = "shopImgList")
    private List<String> shopImgList;

    @ApiModelProperty(value = "店铺联系人",name = "storeUserReq")
    private List<StoreUserRes> storeUserResList;

    @ApiModelProperty(value = "资质等其他图片",name = "otherImgList")
    private List<String> otherImgList;

    @ApiModelProperty(value = "店铺账号信息",name = "storeAccountReq")
    private StoreAccountRes storeAccountRes;

    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "驳回详情",name = "rejectDetail")
    private String rejectDetail;

    @ApiModelProperty("营业时间")
    private String onTimeArr;

    @ApiModelProperty("店铺评分")
    private StoreCommentStaticsRes commentStatics;

    @ApiModelProperty(value = "纬度", name = "latitude")
    private Float latitude;

    @ApiModelProperty(value = "经度", name = "longitude")
    private Float longitude;

    @ApiModelProperty(value = "平台服务费率",name = "platformFee")
    private BigDecimal platformFee;
}
