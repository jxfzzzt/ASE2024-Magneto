package com.car.order.web.model.groupbuy;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.car.common.datasource.model.BaseModelInfo;

import lombok.Data;

@Data
@Table(name = "groupbuy")
public class Groupbuy extends BaseModelInfo {

	/**
	 * 售价
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 成团数量
	 */
	@Column(name = "user_Num")
	private Integer userNum;

	/**
	 * 当前参与人数：因订单状态的反复，此值为大致测算，不等于真正成交订单数量
	 */
	@Column(name = "participate_num")
	private Integer participateNum;

	/**
	 * 团购开始时间
	 */
	@Column(name = "start_Time")
	private Date startTime;

	/**
	 * 团购结束时间
	 */
	@Column(name = "end_Time")
	private Date endTime;

	/**
	 * 状态：0=待开始;1=进行中;2=已结束
	 */
	@Column(name = "group_Sts")
	private Integer groupSts;

	/**
	 * 所属人
	 */
	@Column(name = "user_uuid")
	private String userUuid;

	/**
	 * 成团时间
	 */
	@Column(name = "into_group_time")
	private Date intoGroupTime;

	/**
	 * 配送方式 0快递,1到店服务,2上门服务
	 */
	@Column(name = "receive_Method")
	private Integer receiveMethod;
}
