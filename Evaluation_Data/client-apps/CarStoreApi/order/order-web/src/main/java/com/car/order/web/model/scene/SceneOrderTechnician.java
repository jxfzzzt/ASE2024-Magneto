package com.car.order.web.model.scene;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Table;

/**
 * 现场订单技师相关
 * 
 * @author cjw
 * @date 2021-02-26 22:08:47
 */
@Data
@Table(name = "scene_order_technician")
public class SceneOrderTechnician extends BaseModelInfo {

	/**
	 * 订单uuid
	 */
	private String orderUuid;
	/**
	 * 技师uuid
	 */
	private String technicianUuid;
	/**
	 * 技师姓名
	 */
	private String technicianName;
	/**
	 * 技师手机号
	 */
	private String technicianMobile;
	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 类型1上门，2完成维修
	 */
	private Integer type;

	/**
	 * 已检过程
	 */
	private String alreadyInspect;
	/**
	 * DTC故障code
	 */
	private String dtcCode;
	/**
	 * 维修总结
	 */
	private String repairSummary;
	/**
	 * 故障是否解决 0解决，1未解决
	 */
	private Integer faultSolve;
}
