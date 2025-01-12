package com.car.order.web.mapper.consult;

import com.car.order.client.request.order.consult.QueryConsultListReq;
import com.car.order.client.request.order.consult.QueryOrderConsultListReq;
import com.car.order.client.response.order.consult.ConsultInfoListRes;
import com.car.order.client.response.order.consult.OrderConsultInfoListRes;
import com.car.order.client.response.order.consult.ConsultOrderDetailRes;
import com.car.order.web.model.consult.ConsultOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Repository
public interface ConsultOrderMapper extends Mapper<ConsultOrder> {

    /**
     * 查询咨询订单列表
     * @param param
     * @return
     */
    List<OrderConsultInfoListRes> queryOrderConsultList(QueryOrderConsultListReq param);

    /**
     * 查询咨询订单详情
     * @param uuid
     * @return
     */
    ConsultOrderDetailRes queryOrderConsultDetail(String uuid);

    /**
     * 查询咨询列表
     * @param param
     * @return
     */
    List<ConsultInfoListRes> queryConsultList(QueryConsultListReq param);


    /**
     * 查询我的提问数量
     * @param userUuid
     * @return
     */
    Integer queryQuizCount(@Param("userUuid") String userUuid);
}
