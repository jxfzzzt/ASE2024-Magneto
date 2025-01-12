package cn.qs.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qs.bean.order.Order;
import cn.qs.controller.AbstractUUIDController;
import cn.qs.service.BaseService;
import cn.qs.service.order.OrderService;

@Controller
@RequestMapping("order")
public class OrderController extends AbstractUUIDController<Order> {

	@Autowired
	private OrderService orderService;
	
	@Override
	public String getViewBasePath() {
		return "order";
	}

	@Override
	public BaseService<Order, String> getBaseService() {
		return orderService;
	}

}
