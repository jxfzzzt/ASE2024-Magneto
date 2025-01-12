package com.car.order.client.response.order.goods;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GoodsRes {

	/**
	 * 商品名称
	 */
	@ApiModelProperty("商品名称")
	private String goodsName;

	/**
	 * 
	 */
	@ApiModelProperty("店铺uuid")
	private String storeUuid;

	/**
	 * 
	 */
	@ApiModelProperty("商品类型")
	private String goodsType;

	/**
	 * 
	 */
	@ApiModelProperty("父类型 一级分类")
	private String parentType;

	/**
	 * 
	 */
	@ApiModelProperty("二级分类")
	private String subType;

	/**
	 *
	 */
	@ApiModelProperty(" 工时费 / 服务费")
	private BigDecimal manHourCost;

	/**
	 * 
	 */
	@ApiModelProperty("材料费 / 快递费")
	private BigDecimal materialsExpenses;

	/**
	 * 
	 */
	@ApiModelProperty("销量")
	private Integer salesNum;

	/**
	 * 
	 */
	@ApiModelProperty("库存")
	private Integer surplusNum;

	/**
	 * 
	 */
	@ApiModelProperty("销售状态:0 库存 下架 1 在售 上架")
	private Integer sellSts;

	/**
	 * 
	 */
	@ApiModelProperty("描述")
	private String goodsDescribe;

	/**
	 * 
	 */
	@ApiModelProperty("商品总金额")
	private BigDecimal amt;

	/**
	 * 
	 */
	@ApiModelProperty("平台服务费用")
	private BigDecimal platformServiceMoney;

	/**
	 * 
	 */
	@ApiModelProperty("车辆品牌")
	private BigDecimal vehicleBrand;

	/**
	 * 
	 */
	@ApiModelProperty("车型类型")
	private BigDecimal vehicleModel;

	
	@ApiModelProperty("商品图")
	private String imgUrl;

}
