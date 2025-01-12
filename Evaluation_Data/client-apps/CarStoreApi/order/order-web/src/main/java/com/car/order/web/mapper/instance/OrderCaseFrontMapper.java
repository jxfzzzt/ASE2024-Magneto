package com.car.order.web.mapper.instance;

import com.car.order.client.request.order.instance.QueryOrderCaseListReq;
import com.car.order.client.response.order.instance.OrderCaseFrontListRes;
import com.car.order.client.response.order.instance.OrderCaseFrontRes;
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
public interface OrderCaseFrontMapper extends Mapper<OrderCase> {

    /**
     * 查询案例订单列表
     * @param orderCaseDto
     * @return
     */
    List<OrderCaseFrontListRes> queryOrderCaseInfoList(@Param("orderCaseDto") OrderCaseDto orderCaseDto);

    /**
     * 查询案例订单详情
     * @param uuid
     * @return
     */
    OrderCaseFrontRes queryOrderCaseDetail(String uuid);

    /**
     * 查询订单各状态数量
     * @param orderSts
     * @param technicianUuid
     * @param carOwnerUuid
     * @return
     */
    Integer queryOrderStsNum(@Param("orderSts") Integer orderSts, @Param("technicianUuid") String technicianUuid, @Param("carOwnerUuid") String carOwnerUuid);

    /**
     * 查询未支付完成的案例订单信息
     * @param caseUuid
     * @param userUuid
     * @return
     */
    OrderCase queryOrderCaseInfo(@Param("caseUuid") String caseUuid, @Param("userUuid") String userUuid);
}
