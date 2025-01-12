package com.car.order.web.mapper.order;

import com.car.order.web.model.goods.OrderGoods;
import com.car.order.web.model.instance.OrderCase;
import com.car.order.web.model.order.OrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/8
 */
@Repository
public interface OrderInfoMapper  extends Mapper<OrderInfo> {


    /**
     * 修改订单支付状态
     * @param orderInfo
     * @return
     */
    int updateRefund(@Param("orderInfo") OrderInfo orderInfo);

    /**
     * 根据orderUuid查询相关订单信息
     * @param orderUuid
     * @return
     */
    OrderInfo queryOrderInfo(@Param("orderUuid") String orderUuid);


    /**
     * 查询付款完成7天后的订单
     * @param orderSts
     * @return
     */
    List<OrderInfo> orderGoodsBySts(@Param("orderSts")Integer orderSts);
}
