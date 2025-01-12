package com.car.account.client.request.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianReq {

    @ApiModelProperty("头像图片地址")
    private String photoImgUrl;

    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @NotBlank(message = "请输入用户名！")
    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号码",name = "mobile")
    @NotNull(message = "mobile is null")
    private String mobile;

    @ApiModelProperty(value = "证件类型",name = "certificateType")
    private String certificateType;

    @ApiModelProperty(value = "证件号码",name = "certificateNum")
    private String certificateNum;

    @NotBlank(message = "请选择城市！")
    @ApiModelProperty(value = "城市",name = "addressCity")
    private String addressCity;

    @NotBlank(message = "请选择省份！")
    @ApiModelProperty(value = "省份",name = "addressProvince")
    private String addressProvince;

    @ApiModelProperty(value = "县镇",name = "addressCounty")
    private String addressCounty;

    @NotBlank(message = "请输入详细地址！")
    @ApiModelProperty(value = "详细地址",name = "addressDetail")
    private String addressDetail;

    @ApiModelProperty(value = "技术类型",name = "technologyType")
    @NotNull(message = "technologyType is null")
    private String technologyType;

    @ApiModelProperty(value = "直属亲戚手机",name = "relativeMobile")
    private String relativeMobile;

    @ApiModelProperty(value = "驾驶证正面",name = "driverLicenseUrl")
    private String driverLicenseUrl;

    @ApiModelProperty(value = "驾驶证反面",name = "driverLicenseBackUrl")
    private String driverLicenseBackUrl;

    @ApiModelProperty(value = "身份证正面",name = "identityCardUrl")
    private String identityCardUrl;

    @ApiModelProperty(value = "身份证反面",name = "identityCardBackUrl")
    private String identityCardBackUrl;

    @ApiModelProperty(value = "无犯罪记录证明",name = "noCrimeUrl")
    private String noCrimeUrl;

    @ApiModelProperty(value = "健康检查证明",name = "healthCheckUrl")
    private String healthCheckUrl;

    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "驳回详情",name = "rejectDetail")
    private String rejectDetail;

    @ApiModelProperty(value = "关联店铺",name = "relationStoreUuid")
    private String relationStoreUuid;

    @ApiModelProperty(value = "技师账户信息",name = "technicianAccount")
    private TechnicianAccountReq technicianAccount;

    @ApiModelProperty(value = "技师维修品牌",name = "brandList")
    private List<String> brandUuidList;

    @ApiModelProperty(value = "技师等级:国家等级鉴定 对应字典表uuid",name = "stateVerification")
    private String stateVerification;

    @ApiModelProperty(value = "国家等级鉴定技师证书图片",name = "stateImgList")
    private List<String> stateImgList;

    @ApiModelProperty(value = "技师等级:主机厂认证 对应字典表uuid",name = "hostAuthentication")
    private String hostAuthentication;

    @ApiModelProperty(value = "主机厂认证技师证书图片",name = "hostImgList")
    private List<String> hostImgList;

    @ApiModelProperty("工龄")
    private Integer workingYear;

    @ApiModelProperty("问答金额")
    private BigDecimal answerAmt;

    @ApiModelProperty("问答权重排序")
    private Integer answerSortWeight;
    
    @ApiModelProperty("车友邦技能等级标签(0=普通,1=专家)")
    private Integer cybAuth;
}
