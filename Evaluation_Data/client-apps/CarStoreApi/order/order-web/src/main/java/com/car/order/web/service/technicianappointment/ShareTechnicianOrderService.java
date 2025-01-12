package com.car.order.web.service.technicianappointment;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.technicianappointment.QueryShareTechnicianOrderReq;
import com.car.order.client.request.technicianappointment.ShareTechnicianOrderReq;
import com.car.order.client.request.technicianappointment.UpdateShareTechnicianOrderReq;
import com.car.order.client.response.technicianappointment.ShareTechnicianOrderInfoRes;
import com.car.order.client.response.technicianappointment.ShareTechnicianOrderRes;

import java.util.List;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.web.service
 * @NAME: TechnicianAppointmentOrderService
 * @DATE: 2021/3/4 21:01
 */
public interface ShareTechnicianOrderService {

    /**
     * 新增预约技师订单信息
     * @param req
     * @return
     */
    ResultRes<String> saveTechnicianAppointment(ShareTechnicianOrderReq req);

    /**
     * 查询预约技师订单列表
     * @return
     */
    PageRes<List<ShareTechnicianOrderRes>> queryShareTechnicianOrderList(QueryShareTechnicianOrderReq req);


    /**
     * 完成预约技师订单信息
     * @param uuid
     * @return
     */
    ResultRes updateShareTechnicianOrder(String uuid);

    /**
     * 申请退款预约技师订单
     * @param uuid
     * @return
     */
    ResultRes applicationRefundShareTechnicianOrder(String uuid);

    /**
     * 查询订单详情
     * @param uuid
     * @return
     */
    ResultRes<ShareTechnicianOrderInfoRes> queryShareTechnicianOrder(String uuid);

    /**
     * 技师同意接单
     * @param uuid
     * @return
     */
    ResultRes receiveShareTechnicianOrder(String uuid);
}
