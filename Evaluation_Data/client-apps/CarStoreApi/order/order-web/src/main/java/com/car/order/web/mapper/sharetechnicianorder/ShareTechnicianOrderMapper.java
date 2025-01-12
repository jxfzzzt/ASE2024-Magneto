package com.car.order.web.mapper.sharetechnicianorder;

import com.car.order.web.model.sharetechnicianorder.ShareTechnicianOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.web.mapper.sharetechnicianorder
 * @NAME: ShareTechnicianOrderMapper
 * @DATE: 2021/3/4 23:34
 */
@Repository
public interface ShareTechnicianOrderMapper extends Mapper<ShareTechnicianOrder> {

    /**
     * 查询预约技师订单列表
     *
     * @return
     */
    List<ShareTechnicianOrder> queryShareTechnicianOrderList(@Param("orderStatus") List<Integer> orderStatus, @Param("userUuid") String userUuid, @Param("userType") Integer userType);


    /**
     * 查询共享技师订单详情
     * @param orderUuid
     * @return
     */
    ShareTechnicianOrder queryOrderShareTechnicianOrderInfo(@Param("orderUuid") String orderUuid);

    /**
     * 查询用户订单数量
     * @param orderStatus
     * @param userUuid
     * @param technicianUuid
     * @return
     */
    Integer queryOrderStsNum(@Param("orderStatus")List<Integer> orderStatus, @Param("userUuid")String userUuid, @Param("technicianUuid")String technicianUuid);
}
