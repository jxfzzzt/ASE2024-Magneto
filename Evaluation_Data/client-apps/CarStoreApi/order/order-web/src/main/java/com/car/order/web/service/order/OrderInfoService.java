package com.car.order.web.service.order;

import com.car.common.res.ResultRes;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.request.order.order.ConfirmOrderReq;
import com.car.order.client.request.order.order.OrderWhetherAgreeRefundReq;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/8
 */
public interface OrderInfoService {

    /**
     * 新增订单
     * @param addOrderInfoReq
     * @return
     */
    ResultRes<String> addOrder(AddOrderInfoReq addOrderInfoReq);


    /**
     * 确认订单
     * @param req
     * @return
     */
    ResultRes<String> confirmOrder(ConfirmOrderReq req);

    /**
     * 订单退款
     * @param orderUuid
     * @return
     */
    ResultRes<String> orderRefund(String orderUuid);

    /**
     * 是否同意订单退款
     * @param req
     * @return
     */
    ResultRes<String> orderWhetherAgreeRefund(OrderWhetherAgreeRefundReq req);


    /**
     * 根据id查询订单状态详情
     * @param orderInfoUuid
     * @return
     */
    ResultRes<Boolean> queryOrderSts(String orderInfoUuid);
}
