package com.car.account.client.response.store;

import com.car.account.client.response.store.sub.ImgStoreRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value = "QueryStoreListRes", description = "店铺列表VO")
public class QueryStoreListRes {

	@ApiModelProperty(value = "uuid", name = "uuid")
	private String uuid;

	@ApiModelProperty(value = "品牌", name = "brand")
	private String brand;

	@ApiModelProperty(value = "店铺名称", name = "storeName")
	private String storeName;

	@ApiModelProperty(value = "店铺类型,对应字典表 uuid", name = "storeType")
	private String storeType;

	@ApiModelProperty(value = "店铺类型名称", name = "storeTypeName")
	private String storeTypeName;

	@ApiModelProperty(value = "省份", name = "公司地址-省,对应地区表uuid")
	private String companyAddressProvince;

	@ApiModelProperty(value = "省份名称", name = "addressProvinceName")
	private String addressProvinceName;

	@ApiModelProperty(value = "城市名称", name = "addressCityName")
	private String addressCityName;

	@ApiModelProperty(value = "城市", name = "companyAddressCity")
	private String companyAddressCity;

	@ApiModelProperty(value = "管理员姓名", name = "name")
	private String userName;

	@ApiModelProperty(value = "联系人手机号", name = "mobile")
	private String mobile;

	@ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)", name = "checkSts")
	private Integer checkSts;

	@ApiModelProperty("商铺图片列表")
	private List<ImgStoreRes> imgList;

	@ApiModelProperty("评论条数")
	private Integer commentNum;

	@ApiModelProperty("评论分数")
	private BigDecimal commentScore;

	@ApiModelProperty("服务分类")
	private List<String> classifyList;
	@ApiModelProperty("支持的品牌名称集合")
	private List<String> configNameList;

	@ApiModelProperty("营业时间")
	private String onTimeArr;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;

	@ApiModelProperty("父类型名称")
	private String parentTypeName;
}
