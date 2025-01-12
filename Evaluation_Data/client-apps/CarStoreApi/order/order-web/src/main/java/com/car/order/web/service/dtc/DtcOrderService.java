package com.car.order.web.service.dtc;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcOrderReq;
import com.car.order.client.request.dtc.QueryDtcOrderListReq;
import com.car.order.client.request.order.goods.QueryOrderGoodsListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderListRes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
public interface DtcOrderService {

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    ResultRes<QueryDtcOrderInfoRes> getById(String uuid);


    /**
     * 新增DTC订单信息
     * @param req
     * @return
     */
    ResultRes<String> addOrder(AddDtcOrderReq req);


    /**
     * 分页列表
     * @param req
     * @return
     */
    PageRes<List<QueryDtcOrderListRes>> list(@RequestBody QueryDtcOrderListReq req);

    /**
     * 分页列表
     * @param req
     * @return
     */
    PageRes<List<QueryDtcOrderListRes>> myList(@RequestBody QueryDtcOrderListReq req);

    /**
     * DTC订单信息导出
     * @param exportReq
     * @param response
     */
    void exportOrderDtcList(QueryDtcOrderListReq exportReq, HttpServletResponse response);


    /**
     * 根据id查询订单状态
     * @param uuid
     * @return
     */
    ResultRes<Boolean> queryOrderSts(String uuid);
}
