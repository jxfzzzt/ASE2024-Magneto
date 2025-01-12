package com.car.order.web.model.groupbuy;

import javax.persistence.Column;
import javax.persistence.Table;

import com.car.common.datasource.model.BaseModelInfo;

import lombok.Data;

@Data
@Table(name = "groupbuy_goods")
public class GroupbuyGoods extends BaseModelInfo {

	/**
	 * 商品id
	 */
	@Column(name = "goods_uuid")
	private String goodsUuid;

	/**
	 * 团购id
	 */
	@Column(name = "groupbuy_uuid")
	private String groupbuyUuid;
}
