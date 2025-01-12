package com.car.order.web.model.groupbuy;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.car.common.datasource.model.BaseModelInfo;

import lombok.Data;

@Data
@Table(name = "order_groupbuy")
public class OrderGroupbuy extends BaseModelInfo {

	/**
	 * 订单编号
	 */
	@Column(name = "order_num")
	private String orderNum;

	/**
	 * 团购uuid
	 */
	@Column(name = "groupbuy_uuid")
	private String groupbuyUuid;

	/**
	 * 购买人
	 */
	@Column(name = "user_uuid")
	private String userUuid;

	/**
	 * 实付金额
	 */
	@Column(name = "pay_fee")
	private BigDecimal payFee;

	/**
	 * 支付方式 0 微信支付 1 支付宝支付
	 */
	@Column(name = "pay_type")
	private Integer payType;

	/**
	 * 支付时间
	 */
	@Column(name = "pay_time")
	private Date payTime;

	/**
	 * 申请退款时间
	 */
	@Column(name = "back_apply_time")
	private Date backApplyTime;

	/**
	 * 真实退款时间
	 */
	@Column(name = "back_time")
	private Date backTime;

	/**
	 * 退款金额
	 */
	@Column(name = "back_fee")
	private BigDecimal backFee;

	/**
	 * 退款账户
	 */
	@Column(name = "back_account")
	private String backAccount;

	/**
	 * 0待支付；1待成团；2待预约；3取消待审核；4已取消；5待上门；6上门待确认；7待使用；8完成待确认；9已完成
	 */
	@Column(name = "order_sts")
	private Integer orderSts;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 平台补贴
	 */
	@Column(name = "sys_subsidy")
	private BigDecimal sysSubsidy;

	/**
	 * 平台服务费
	 */
	@Column(name = "platform_service_money")
	private BigDecimal platformServiceMoney;
}
