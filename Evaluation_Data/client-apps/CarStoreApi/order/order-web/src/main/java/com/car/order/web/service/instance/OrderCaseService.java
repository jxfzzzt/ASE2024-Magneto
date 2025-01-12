package com.car.order.web.service.instance;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.instance.OrderCaseInfoListRes;
import com.car.order.client.response.order.instance.OrderCaseDetailRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
public interface OrderCaseService {

    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    PageRes<List<OrderCaseInfoListRes>> queryOrderCaseList(QueryOrderCaseListReq param);

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    ResultRes<OrderCaseDetailRes> queryOrderCaseDetail(String uuid);

    /**
     * 案例订单信息导出
     * @param exportReq
     * @param response
     */
    void exportOrderDrivingList(QueryOrderCaseListReq exportReq, HttpServletResponse response);
}
