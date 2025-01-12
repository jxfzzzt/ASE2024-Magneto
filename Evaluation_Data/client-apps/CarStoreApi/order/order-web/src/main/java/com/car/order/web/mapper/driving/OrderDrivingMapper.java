package com.car.order.web.mapper.driving;

import com.car.order.client.request.order.driving.QueryOrderDrivingListReq;
import com.car.order.client.response.order.driving.OrderDrivingInfoListRes;
import com.car.order.web.dto.OrderDrivingDto;
import com.car.order.web.model.driving.OrderDriving;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Repository
public interface OrderDrivingMapper extends Mapper<OrderDriving> {

    /**
     * 查询代驾订单列表
     * @param param
     * @return
     */
    List<OrderDrivingInfoListRes> queryOrderDrivingList(QueryOrderDrivingListReq param);

    /**
     * 查询代驾订单详情
     * @param uuid
     * @return
     */
    OrderDrivingDto queryOrderDrivingDetail(String uuid);
}
