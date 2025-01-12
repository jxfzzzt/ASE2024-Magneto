package com.car.utility.client.feign;


import com.car.common.res.ResultRes;
import com.car.utility.client.request.pay.CreateOrderReq;
import com.car.utility.client.response.pay.CreateOrderRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author
 * @date 2020/6/15 11:47
 */
@FeignClient(name = "utility")
public interface PayFeign {

    /**
     * 统一创建支付订单接口
     * @param param
     * @return
     */
    @PostMapping(value = "pay/createPayOrder")
    ResultRes<CreateOrderRes> createPayOrder(@RequestBody @Valid CreateOrderReq param);

    /**
     * 统一退款订单接口
     * @param orderNo
     * @return
     */
    @PostMapping(value = "pay//orderRefund/{orderNo}")
    ResultRes orderRefund(@PathVariable("orderNo") String orderNo);

}
