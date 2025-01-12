package com.car.order.web.service.front.goods;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.front.QueryOrderStsNumReq;
import com.car.order.client.request.order.goods.QueryOrderGoodsFrontListReq;
import com.car.order.client.response.order.front.QueryOrderStsNumRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontRes;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
public interface OrderGoodsFrontService {


    /**
     * 查询维修保养订单
     * @param param
     * @return
     */
    PageRes<List<OrderGoodsFrontListRes>> queryOrderGoodsList(QueryOrderGoodsFrontListReq param);

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    ResultRes<OrderGoodsFrontRes> queryOrderGoodsDetail(String uuid);

    /**
     * 查询订单各状态数量
     * @param req
     * @return
     */
    ResultRes<QueryOrderStsNumRes> queryOrderStsNum(QueryOrderStsNumReq req);

}
