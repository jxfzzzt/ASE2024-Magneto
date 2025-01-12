package com.car.utility.web.service;

import com.car.common.res.ResultRes;
import com.car.utility.client.request.pay.CreateOrderReq;
import com.car.utility.client.response.pay.CreateOrderRes;
import com.car.utility.client.response.pay.PayOrderRes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xlj
 */
public interface PayService {

    /**
     * 统一创建支付订单接口
     * @param param
     * @return
     */
    ResultRes<CreateOrderRes> createPayOrder(CreateOrderReq param);

    /**
     * 微信支付下单异步回调
     * @param paraMap
     * @return
     */
    String weixinBack(Map<String, String> paraMap);

    /**
     * 根据订单号查询支付记录
     * @param orderNo
     * @return
     */
    ResultRes<PayOrderRes> queryPayByOrderNo(String orderNo);

    /**
     * 统一退款订单接口
     * @param orderNo
     * @return
     */
    ResultRes orderRefund(String orderNo);

    /**
     * 微信退款
     * @param paraMap
     * @return
     */
    String weChartBackRefund(Map<String, String> paraMap);

    /**
     * 支付宝支付下单异步回调
     * @param request
     * @return
     */
    String aliPayBack(HttpServletRequest request);
}
