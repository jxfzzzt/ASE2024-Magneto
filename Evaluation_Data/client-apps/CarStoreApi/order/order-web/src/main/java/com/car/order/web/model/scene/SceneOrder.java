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
@Table(name = "scene_order")
public class SceneOrder extends BaseModelInfo {

	/**
	 * 订单编号
	 */
	private String orderNum;
	/**
	 * dtc故障uuid
	 */
	private String dtcUuid;
	/**
	 * 发布者uuid
	 */
	private String issuerUuid;
	/**
	 * 发布者姓名
	 */
	private String issuerName;
	/**
	 * 发布者手机号码
	 */
	private String issuerMobile;
	/**
	 * 品牌uuid
	 */
	private String brandUuid;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 车型uuid
	 */
	private String carModelUuid;
	/**
	 * 车型名称
	 */
	private String carModelName;
	/**
	 * 车款
	 */
	private String carStyle;
	/**
	 * VIN码
	 */
	private String vinCode;
	/**
	 * 变速器一级uuid(对应字典uuid)
	 */
	private String transmissionOneLevelUuid;
	/**
	 * 变速器二级（对应字典uuid）
	 */
	private String transmissionTwoLevelUuid;
	/**
	 * 发动机排量uuid(对应字典uuid)
	 */
	private String engineDisplacementUuid;
	/**
	 * 驱动方式Uuid(对应字典uuid)
	 */
	private String drivingModeUuid;
	/**
	 * 增压系统uuid(对应字典uuid)
	 */
	private String boosterSystemUuid;
	/**
	 * 保修状态
	 */
	private Integer warrantySts;
	/**
	 * 其他状态
	 */
	private String otherSts;
	/**
	 * 故障描述
	 */
	private String faultDesc;
	/**
	 * 维修类型uuid（对应字典表uuid）
	 */
	private String repairTypeUuid;
	/**
	 * 已检过程
	 */
	private String alreadyInspect;
	/**
	 * DTC故障code
	 */
	private String dtcCode;
	/**
	 * 基本上门费用
	 */
	private BigDecimal basicDoorAmount;

	/**
	 * 基本上门费用Uuid
	 */
	private String basicDoorAmountUuid;

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
	/**
	 * 抢单状态 0未抢，1已抢
	 */
	private Integer grabbingOrdersSts;
	/**
	 * 购买者uuid
	 */
	private String buyerUuid;
	/**
	 * 购买者姓名
	 */
	private String buyerName;
	/**
	 * 购买者手机号码
	 */
	private String buyerMobile;
	/**
	 * 详细地址
	 */
	private String detailedAddr;
	/**
	 * 经度
	 */
	private Float longitude;
	/**
	 * 纬度
	 */
	private Float latitude;
	/**
	 * 抢单人类型 1:车主，2：技师，3:店铺
	 */
	private Integer confirmType;

	/**
	 * 服务时间
	 */
	private String  serviceDate;

	/**
	 * 上门uuId
	 */
	private String inDoorUuid;


	/**
	 * 完成服务uuId
	 */
	private String serviceEndUuId;
}
