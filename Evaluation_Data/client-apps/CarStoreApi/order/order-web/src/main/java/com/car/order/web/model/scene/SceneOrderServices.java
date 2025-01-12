package com.car.order.web.model.scene;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 现场订单信息
 * 
 * @author cjw
 * @date 2021-02-26 22:08:48
 */
@Data
@Table(name = "scene_order_service")
public class SceneOrderServices extends BaseModelInfo {

	/**
	 * 订单编号
	 */
	private String orderNum;


	/**
	 * 故障描述
	 */
	private String faultDesc;

	/**
	 * 解决方案
	 */
	private String solution;

	/**
	 * 基本检查费用
	 */
	private BigDecimal basicInspectAmount;

	/**
	 * 基本检查费用Uuid
	 */
	private String basicInspectAmountUuid;

	/**
	 * 相关线路检查费用
	 */
	private BigDecimal lineInspectAmount;

	/**
	 * 相关线路检查费用Uuid
	 */
	private String lineInspectAmountUuid;

	/**
	 * 诊断仪使用费
	 */
	private BigDecimal diagnosisInstrumentAmount;

	/**
	 * 诊断仪使用费Uuid
	 */
	private String diagnosisInstrumentAmountUuid;

	/**
	 * 车辆钣金修复费用
	 */
	private BigDecimal carSheetMetalAmount;

	/**
	 * 车辆钣金修复费用Uuid
	 */
	private String carSheetMetalAmountUuid;

	/**
	 * 车辆油漆修复费用
	 */
	private BigDecimal carPaintRepairAmount;

	/**
	 * 车辆油漆修复费用Uuid
	 */
	private String carPaintRepairAmountUuid;

	/**
	 * 其他费用费
	 */
	private BigDecimal otherAmount;

	/**
	 * 其他费用费Uuid
	 */
	private String otherAmountUuid;

	/**
	 * 平台订单服务费
	 */
	private BigDecimal orderServiceAmount;

	/**
	 * 平台订单服务费Uuid
	 */
	private String orderServiceAmountUuid;

	/**
	 * 总支付费用
	 */
	private BigDecimal totalAmount;

	/**
	 * 支付方式 0 微信支付 1 支付宝支付
	 */
	private Integer payType;
	/**
	 * 订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败 6：已完成
	 */
	private Integer orderSts;

}
