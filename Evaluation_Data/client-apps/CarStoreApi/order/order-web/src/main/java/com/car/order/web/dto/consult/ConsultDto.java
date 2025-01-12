package com.car.order.web.dto.consult;

import com.car.order.web.model.consult.Consult;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;

/**
 * @author zhangyp
 * @date 2021/1/28 0:37
 */
@Data
public class ConsultDto extends Consult {

	/**
	 * 咨询描述
	 */
	private String consultDesc;

	/**
	 * 回答描述
	 */
	private String answerDesc;

	/**
	 * 订单uuid
	 */
	private String orderUuid;

	/**
	 * 订单状态
	 */
	private Integer orderSts;

	/**
	 * 咨询金额
	 */
	private BigDecimal consultAmt;

	/**
	 * 车辆品牌
	 */
	private String vehicleBrand;

	/**
	 * 车型类型
	 */
	private String vehicleModel;
}
