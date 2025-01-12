package com.car.utility.web.mapper;

import com.car.utility.web.model.PayOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface PayMapper extends Mapper<PayOrder> {

    /**
     * 将订单状态修改为支付成功
     * @param orderNo
     * @param orderState
     */
    void updateOrderStatus(@Param("orderNo") String orderNo, @Param("orderState") Integer orderState);

    /**
     * 修改订单ID
     * @param uuid
     * @param newUuid
     */
    void updatePayOrderUuid(@Param("uuid") String uuid, @Param("newUuid") String newUuid);
}
