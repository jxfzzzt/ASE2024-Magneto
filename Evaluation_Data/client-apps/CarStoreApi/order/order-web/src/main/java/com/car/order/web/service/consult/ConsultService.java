package com.car.order.web.service.consult;

import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.response.order.consult.ConsultBaseRes;
import com.car.order.client.response.order.consult.ConsultRes;

import java.util.List;

/**
 * 咨询
 * @author zhangyp
 * @date 2021/1/28 0:19
 */
public interface ConsultService {

    /**
     * 问题列表> 车主可咨询
     * @param pageReq
     * @return
     */
   PageRes<List<ConsultBaseRes>> queryConsultList(PageReq pageReq);

    /**
     * 技师待回答列表
     * @param pageReq
     * @return
     */
   PageRes<List<ConsultRes>> queryPreConsultList(PageReq pageReq);

    /**
     * 查询咨询和问答详情
     * @param uuid
     * @return
     */
    ResultRes<ConsultRes> queryDetail(String uuid);
}
