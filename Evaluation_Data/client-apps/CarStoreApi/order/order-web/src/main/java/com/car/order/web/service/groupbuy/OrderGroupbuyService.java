package com.car.order.web.service.groupbuy;

import java.util.List;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.groupbuy.CreateOrderGroupbuyReq;
import com.car.order.client.request.order.groupbuy.QueryOrderGroupbuyListReq;
import com.car.order.client.response.order.groupbuy.OrderGroupbuyRes;

public interface OrderGroupbuyService {

	String create(CreateOrderGroupbuyReq params);

	ResultRes<OrderGroupbuyRes> queryByUuid(String uuid);

	ResultRes<String> updateGroupbuyEnd(String uuid);

	PageRes<List<OrderGroupbuyRes>> queryOrderGroupbuyList(QueryOrderGroupbuyListReq params);

}
