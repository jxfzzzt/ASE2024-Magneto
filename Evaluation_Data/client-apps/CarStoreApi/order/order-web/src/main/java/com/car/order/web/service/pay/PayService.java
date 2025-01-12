package com.car.order.web.service.pay;

import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.AddAuditorReq;
import com.car.order.client.request.order.consult.PayConsultReq;
import com.car.order.client.request.order.order.PayReq;
import com.car.utility.client.response.pay.CreateOrderRes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/7
 */
public interface PayService {

    /**
     * 咨询订单支付
     * @param req
     * @return
     */
    ResultRes<CreateOrderRes> payConsultOrder(PayReq req, HttpServletRequest request);


    /**
     * 修改订单支付状态
     * @param orderInfoUuid
     * @return
     */
    ResultRes<String> updateOrderPaySts(String orderInfoUuid);

}
