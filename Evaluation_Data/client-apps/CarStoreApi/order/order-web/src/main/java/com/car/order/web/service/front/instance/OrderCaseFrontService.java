package com.car.order.web.service.front.instance;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.instance.QueryOrderCaseFrontListReq;
import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.instance.OrderCaseDetailRes;
import com.car.order.client.response.order.instance.OrderCaseFrontListRes;
import com.car.order.client.response.order.instance.OrderCaseFrontRes;
import com.car.order.client.response.order.instance.OrderCaseInfoListRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
public interface OrderCaseFrontService {

    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    PageRes<List<OrderCaseFrontListRes>> queryOrderCaseList(QueryOrderCaseFrontListReq param);

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    ResultRes<OrderCaseFrontRes> queryOrderCaseDetail(String uuid);

    /**
     * 购买案例
     * @param caseUuid
     * @return
     */
    ResultRes<String> order(String caseUuid);

    /**
     * 购买案例
     * @param caseUuid
     * @return
     */
    ResultRes<String> orderTwo(String caseUuid);
}
