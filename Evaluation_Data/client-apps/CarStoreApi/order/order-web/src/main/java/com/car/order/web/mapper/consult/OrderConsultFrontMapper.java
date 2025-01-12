package com.car.order.web.mapper.consult;

import com.car.order.client.response.order.consult.ConsultOrderFrontRes;
import com.car.order.client.response.order.consult.OrderConsultFrontListRes;
import com.car.order.web.dto.OrderConsultDto;
import com.car.order.web.model.content.Content;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Repository
public interface OrderConsultFrontMapper extends Mapper<Content> {

    /**
     * 查询咨询订单
     * @param orderConsultDto
     * @return
     */
    List<OrderConsultFrontListRes> queryContentList(@Param("orderConsultDto") OrderConsultDto orderConsultDto);

    /**
     * 查询咨询订单
     * @param uuid
     * @return
     */
    ConsultOrderFrontRes queryContentDetail(@Param("uuid") String uuid);

    /**
     * 查询订单各状态数量
     * @param orderSts
     * @param serviceSts
     * @param userUuid
     * @return
     */
    Integer queryOrderStsNum(@Param("orderSts") Integer orderSts, @Param("serviceSts")Integer serviceSts, @Param("userUuid") String userUuid,@Param("technicianUuid") String technicianUuid);
}
