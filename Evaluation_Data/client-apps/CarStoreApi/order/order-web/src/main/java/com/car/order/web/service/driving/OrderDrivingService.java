package com.car.order.web.service.driving;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.driving.QueryOrderDrivingListReq;
import com.car.order.client.response.order.driving.OrderDrivingInfoListRes;
import com.car.order.client.response.order.driving.OrderDrivingInfoRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
public interface OrderDrivingService {


    /**
     * 查询代驾订单列表
     * @param param
     * @return
     */
    PageRes<List<OrderDrivingInfoListRes>> queryOrderDrivingList(QueryOrderDrivingListReq param);

    /**
     * 查询代驾订单详情
     * @param uuid
     * @return
     */
    ResultRes<OrderDrivingInfoRes> queryOrderDrivingDetail(String uuid);

    /**
     * 代驾订单信息导出
     * @param exportReq
     * @param response
     */
    void exportOrderDrivingList(QueryOrderDrivingListReq exportReq, HttpServletResponse response);
}
