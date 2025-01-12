package com.car.order.web.service.goods;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.goods.*;
import com.car.order.client.response.order.goods.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
public interface OrderGoodsService {


    /**
     * 查询商品订单列表
     * @param param
     * @return
     */
    PageRes<List<OrderGoodsListRes>> queryOrderGoodsList(QueryOrderGoodsListReq param);

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    ResultRes<OrderGoodsRes> queryOrderGoodsDetail(String uuid);

    /**
     * 商品订单信息导出
     * @param exportReq
     * @param response
     */
    void exportOrderGoodsList(QueryOrderGoodsListReq exportReq, HttpServletResponse response);


    /**
     * 预下单
     * @param params
     * @return
     */
    ResultRes<PreOrderRes> preOrder(PreOrderReq params);

    /**
     * 下单 成功返回订单号
     * @param params
     * @return
     */
    String createOrder(CreateOrderReq params);

    /**
     * 修改订单服务信息
     * @param req
     * @return
     */
    ResultRes<String> updateGoodsOrder(UpdateServerOrderReq req);

    /**
     * 修改订单物流信息
     * @param req
     * @return
     */
    ResultRes<String> updateGoodsDeliveryOrder(UpdateDeliveryOrder req);
}
