package com.car.order.web.mapper.goods;

import com.car.order.client.request.order.goods.QueryOrderGoodsListReq;
import com.car.order.client.request.order.goods.UpdateDeliveryOrder;
import com.car.order.client.request.order.goods.UpdateServerOrderReq;
import com.car.order.client.response.order.goods.OrderGoodsListRes;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.web.model.goods.OrderGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Repository
public interface OrderGoodsMapper extends Mapper<OrderGoods> {


    /**
     * 查询商品订单列表
     * @param param
     * @return
     */
    List<OrderGoodsListRes> queryOrderGoodsList(QueryOrderGoodsListReq param);

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    OrderGoodsRes queryOrderGoods(String uuid);

    /**
     * 修改订单服务信息
     * @param req
     * @return
     */
    int updateGoodsOrder(@Param("req") UpdateServerOrderReq req, @Param("userName") String userName);

    /**
     * 修改订单物流信息
     * @param req
     * @return
     */
    int updateGoodsDeliveryOrder(@Param("req") UpdateDeliveryOrder req, @Param("userName") String userName);
}
