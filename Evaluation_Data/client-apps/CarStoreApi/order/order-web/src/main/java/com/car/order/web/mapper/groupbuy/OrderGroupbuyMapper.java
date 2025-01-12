package com.car.order.web.mapper.groupbuy;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.car.order.client.request.order.groupbuy.QueryOrderGroupbuyListReq;
import com.car.order.client.response.order.groupbuy.OrderGroupbuyRes;
import com.car.order.web.model.groupbuy.OrderGroupbuy;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface OrderGroupbuyMapper extends Mapper<OrderGroupbuy> {

	List<OrderGroupbuyRes> queryOrderGroupbuyList(QueryOrderGroupbuyListReq param);

	/**
	 * 已支付（待成团）的更新为待预约
	 * @param groupbuyUuid
	 */
	void updateGroupbuyEnd1To2(String groupbuyUuid);

	/**
	 * 没支付的订单，直接取消
	 * @param groupbuyUuid
	 */
	void updateGroupbuyEnd0To4(String groupbuyUuid);
}
