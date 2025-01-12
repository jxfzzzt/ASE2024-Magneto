package cn.qs.service.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.order.Order;
import cn.qs.mapper.BaseMapper;
import cn.qs.mapper.order.OrderMapper;
import cn.qs.service.impl.AbastractBaseUUIDServiceImpl;
import cn.qs.service.order.OrderService;
@Service
public class OrderServiceImpl extends AbastractBaseUUIDServiceImpl<Order> implements OrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public BaseMapper<Order, String> getBaseMapper() {
		return orderMapper;
	}

}
