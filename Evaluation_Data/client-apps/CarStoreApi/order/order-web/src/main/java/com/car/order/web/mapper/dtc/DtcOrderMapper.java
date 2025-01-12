package com.car.order.web.mapper.dtc;

import com.car.order.client.request.dtc.QueryDtcOrderListReq;
import com.car.order.client.response.dtc.QueryDtcOrderInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderListRes;
import com.car.order.web.model.dtc.DtcOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/2/17
 */
@Repository
public interface DtcOrderMapper extends Mapper<DtcOrder> {

    /**
     * 根据id查询详情
     * @param orderUuid
     * @return
     */
    QueryDtcOrderInfoRes getById(@Param("orderUuid") String orderUuid);

    /**
     * 查询dtc订单列表
     * @param req
     * @param userUuid
     * @return
     */
    List<QueryDtcOrderListRes> list(@Param("req") QueryDtcOrderListReq req, @Param("userUuid") String userUuid);

    /**
     * 查询dtc订单列表
     * @param req
     * @param userUuid
     * @return
     */
    List<QueryDtcOrderListRes> myList(@Param("req") QueryDtcOrderListReq req, @Param("userUuid") String userUuid);

    /**
     * 查询购买订单数量不大于三的订单数量
     * @param userUuid
     * @param dtcUuid
     * @return
     */
    DtcOrder queryPurchaseOrder(@Param("userUuid") String userUuid, @Param("dtcUuid") String dtcUuid);
}
