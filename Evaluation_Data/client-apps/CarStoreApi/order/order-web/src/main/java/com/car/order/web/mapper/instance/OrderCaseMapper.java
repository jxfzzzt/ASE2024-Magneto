package com.car.order.web.mapper.instance;

import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.instance.OrderCaseInfoListRes;
import com.car.order.web.dto.OrderCaseDto;
import com.car.order.web.model.instance.OrderCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Repository
public interface OrderCaseMapper extends Mapper<OrderCase> {

    /**
     * 查询案例订单列表
     * @param param
     * @return
     */
    List<OrderCaseInfoListRes> queryOrderCaseInfoList(QueryOrderCaseListReq param);

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    OrderCaseDto queryOrderCaseDetail(String uuid);


    /**
     * 查询我的案例数量
     * @param userUuid
     * @return
     */
    Integer queryCaseCount(@Param("userUuid") String userUuid);
}
