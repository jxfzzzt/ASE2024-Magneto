package com.car.order.web.model.scene;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Table;

/**
 * 现场订单图片
 * 
 * @author cjw
 * @date 2021-02-26 22:08:48
 */
@Data
@Table(name = "scene_order_dtc_images")
public class SceneOrderDtcImages extends BaseModelInfo {

	/**
	 * 关联uuid
	 */
	private String relationUuid;
	/**
	 * dtc图片
	 */
	private String dtcImageUrl;
	/**
	 * 状态 0有效1无效
	 */
	private Integer sts;

	/**
	 * 状态 0有效1无效
	 */
	private Integer type;

}
