package com.car.account.client.response.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianRes {
	@ApiModelProperty(value = "数据ID", name = "uuid")
	private String uuid;

	@ApiModelProperty(value = "用户名", name = "userName")
	private String userName;

	@ApiModelProperty(value = "手机号码", name = "mobile")
	private String mobile;

	@ApiModelProperty(value = "证件类型", name = "certificateType")
	private String certificateType;

	@ApiModelProperty(value = "证件号码", name = "certificateNum")
	private String certificateNum;

	@ApiModelProperty(value = "城市", name = "addressCity")
	private String addressCity;

	@ApiModelProperty(value = "省份", name = "addressProvince")
	private String addressProvince;

	@ApiModelProperty(value = "县镇", name = "addressCounty")
	private String addressCounty;

	@ApiModelProperty(value = "详细地址", name = "addressDetail")
	private String addressDetail;

	@ApiModelProperty(value = "技术类型", name = "technologyType")
	private String technologyType;

	@ApiModelProperty(value = "技术类型名称", name = "technologyTypeName")
	private String technologyTypeName;

	@ApiModelProperty(value = "直属亲戚手机", name = "relativeMobile")
	private String relativeMobile;

	@ApiModelProperty(value = "驾驶证正面", name = "driverLicenseUrl")
	private String driverLicenseUrl;

	@ApiModelProperty(value = "驾驶证反面", name = "driverLicenseBackUrl")
	private String driverLicenseBackUrl;

	@ApiModelProperty(value = "身份证正面", name = "identityCardUrl")
	private String identityCardUrl;

	@ApiModelProperty(value = "身份证反面", name = "identityCardBackUrl")
	private String identityCardBackUrl;

	@ApiModelProperty(value = "无犯罪记录证明", name = "noCrimeUrl")
	private String noCrimeUrl;

	@ApiModelProperty(value = "健康检查证明", name = "healthCheckUrl")
	private String healthCheckUrl;

	@ApiModelProperty(value = "订单总数", name = "orderCount")
	private Integer orderCount;

	@ApiModelProperty(value = "案例总数", name = "caseCount")
	private Integer caseCount;

	@ApiModelProperty(value = "问答总数", name = "qaCount")
	private Integer qaCount;

	@ApiModelProperty(value = "现场支持总数", name = "supportCount")
	private Integer supportCount;

	@ApiModelProperty(value = "评分", name = "score")
	private BigDecimal score;

	@ApiModelProperty("工龄")
	private Integer workingYear;

	@ApiModelProperty(value = "评分总次数", name = "scoreCount")
	private Integer scoreCount;

	@ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)", name = "checkSts")
	private Integer checkSts;

	@ApiModelProperty(value = "驳回详情", name = "rejectDetail")
	private String rejectDetail;

	@ApiModelProperty(value = "关联店铺", name = "relationStoreUuid")
	private String relationStoreUuid;

	@ApiModelProperty(value = "技师账户信息", name = "technicianAccount")
	private TechnicianAccountRes technicianAccount;

	@ApiModelProperty(value = "技师维修品牌", name = "brandList")
	private List<TechnicianBrandRes> brandList;

	@ApiModelProperty(value = "头像图片链接地址", name = "photoImgUrl")
	private String photoImgUrl;

	@ApiModelProperty(value = "技师等级:国家等级鉴定 对应字典表uuid", name = "stateVerification")
	private String stateVerification;

	@ApiModelProperty(value = "国家等级鉴定技师证书图片", name = "stateImgList")
	private List<String> stateImgList;

	@ApiModelProperty(value = "技师等级:主机厂认证 对应字典表uuid", name = "hostAuthentication")
	private String hostAuthentication;

	@ApiModelProperty(value = "主机厂认证技师证书图片", name = "hostImgList")
	private List<String> hostImgList;

	@ApiModelProperty(value = "问答金额", name = "answerAmt")
	private BigDecimal answerAmt;

	@ApiModelProperty(value = "技师预约次数")
	private Integer shareNum;

	@ApiModelProperty(value = "技师平台预约费用")
	private Double platformMoney;

	@ApiModelProperty(value = "地址详情经度")
	private Float addressLongitude;

	@ApiModelProperty(value = "地址详情纬度")
	private Float addressLatitude;

	@ApiModelProperty(value = "车友邦技能等级标签(0=普通,1=专家)")
	private Integer cybAuth;
}
