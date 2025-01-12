package com.car.account.client.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value="UpdateStoreReq",description="修改店铺请求VO")
public class UpdateStoreReq {

    @NotBlank(message = "[UpdateStoreReq] uuid is required！")
    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺类型,对应字典表 uuid",name = "storeType")
    private String storeType;

    @ApiModelProperty(value = "汽车品牌列表 uuid",name = "brandUuidList")
    private List<String> brandUuidList;

    @ApiModelProperty(value = "公司名称",name = "companyName")
    private String companyName;

    @ApiModelProperty(value = "公司地址-省",name = "companyAddressProvince")
    private String companyAddressProvince;

    @ApiModelProperty(value = "公司地址-市",name = "companyAddressCity")
    private String companyAddressCity;

    @ApiModelProperty(value = "公司地址-县镇",name = "companyAddressCounty")
    private String companyAddressCounty;

    @ApiModelProperty(value = "公司地址-详细信息",name = "companyAddressDetail")
    private String companyAddressDetail;

    @ApiModelProperty(value = "营业执照图片",name = "businessImgList")
    private List<String> businessImgList;

    @ApiModelProperty(value = "门店图片",name = "shopImgList")
    private List<String> shopImgList;

    @ApiModelProperty(value = "资质等其他图片",name = "otherImgList")
    private List<String> otherImgList;

    @ApiModelProperty(value = "店铺联系人",name = "storeUserReqList")
    private List<StoreUserReq> storeUserReqList;

    @ApiModelProperty(value = "店铺账号信息",name = "storeAccountReq")
    private StoreAccountReq storeAccountReq;

    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    @Max(value = 2, message = "审核状态数值不能大于2")
    @Min(value = 0, message = "审核状态数值不能小于0")
    private Integer checkSts;

    @ApiModelProperty(value = "驳回详情",name = "rejectDetail")
    private String rejectDetail;


    @NotNull(message = "请填写审核状态!")
    @Max(value = 100,message = "服务费率不能大于100")
    @Min(value = 0, message = "服务费率不能小于0")
    @ApiModelProperty(value = "平台服务费率",name = "platformFee")
    private BigDecimal platformFee;

}
