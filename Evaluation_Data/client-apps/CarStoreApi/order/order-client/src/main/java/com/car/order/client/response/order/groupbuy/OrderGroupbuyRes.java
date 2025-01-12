package com.car.order.client.response.order.groupbuy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.car.order.client.response.order.goods.GoodsRes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderGroupbuyRes", description = "团购订单VO")
public class OrderGroupbuyRes {

	@ApiModelProperty("订单编号")
	private String orderNum;

	@ApiModelProperty("团购uuid")
	private String groupbuyUuid;

	@ApiModelProperty("购买人")
	private String userUuid;

	@ApiModelProperty(" 实付金额")
	private BigDecimal payFee;

	@ApiModelProperty("支付方式 0 微信支付 1 支付宝支付")
	private Integer payType;

	@ApiModelProperty(" 支付时间")
	private Date payTime;

	@ApiModelProperty("申请退款时间")
	private Date backApplyTime;

	@ApiModelProperty("真实退款时间")
	private Date backTime;

	@ApiModelProperty("退款金额")
	private BigDecimal backFee;

	@ApiModelProperty("退款账户")
	private String backAccount;

	@ApiModelProperty("0待支付；1待成团；2待预约；3取消待审核；4已取消；5待上门；6上门待确认；7待使用；8完成待确认；9已完成")
	private Integer orderSts;

	@ApiModelProperty("备注")
	private String remark;

	@ApiModelProperty("平台补贴")
	private BigDecimal sysSubsidy;
	@ApiModelProperty("平台服务费")
	private BigDecimal platformServiceMoney;

	@ApiModelProperty("商品集合")
	private List<GoodsRes> goodsRes;

	@ApiModelProperty("团购")
	private GroupbuyRes groupbuyRes;
}
