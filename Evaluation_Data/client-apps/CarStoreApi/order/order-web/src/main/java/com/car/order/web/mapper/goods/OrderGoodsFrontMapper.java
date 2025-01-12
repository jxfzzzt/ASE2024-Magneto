package com.car.order.web.mapper.goods;

import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import com.car.order.client.response.order.goods.OrderGoodsFrontRes;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.web.dto.OrderGoodsDto;
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
public interface OrderGoodsFrontMapper extends Mapper<OrderGoods> {


    /**
     * 查询维修保养订单
     * @param orderGoodsDto
     * @return
     */
    List<OrderGoodsFrontListRes> queryOrderGoodsList(@Param("orderGoodsDto") OrderGoodsDto orderGoodsDto);

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    OrderGoodsFrontRes queryOrderGoods(String uuid);


    /**
     * 查询订单各状态数量
     * @param orderSts
     * @param serviceSts
     * @param userUuid
     * @return
     */
    Integer queryOrderStsNum(@Param("orderSts") Integer orderSts, @Param("serviceSts")Integer serviceSts, @Param("userUuid") String userUuid,@Param("storeUuid") String storeUuid);
}
