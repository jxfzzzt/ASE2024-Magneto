package com.car.order.web.service.consult;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.consult.*;
import com.car.order.client.response.order.consult.ConsultInfoListRes;
import com.car.order.client.response.order.consult.OrderConsultInfoListRes;
import com.car.order.client.response.order.consult.ConsultOrderDetailRes;
import com.car.order.web.model.consult.Consult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/1
 */
public interface OrderConsultService {


    /**
     * 新增咨询订单
     * @param addConsultReq
     * @return
     */
    ResultRes<String> addConsultOrder(AddConsultReq addConsultReq);

    /**
     * 删除咨询订单
     * @param orderUuid
     * @return
     */
    ResultRes<String> deleteConsultOrder(String orderUuid);

    /**
     * 回答咨询订单
     * @param addAnswerConsultReq
     * @return
     */
    ResultRes<String> updateConsultAnswer(AddAnswerConsultReq addAnswerConsultReq);

    /**
     * 是否采纳回答结果
     * @param updateAcceptResultReq
     * @return
     */
    ResultRes<String> updateAcceptResult(UpdateAcceptResultReq updateAcceptResultReq);

    /**
     * 推送内容审核
     * @param orderUuid
     * @return
     */
    ResultRes pushContentCheck(String orderUuid);
    /**
     * 新增旁听订单
     * @param addAuditorReq
     * @return
     */
    ResultRes<String> addAuditorOrder(AddAuditorReq addAuditorReq);

    /**
     * 新增旁听订单
     * @param addAuditorReq
     * @return
     */
    ResultRes<String> addAuditorOrderTwo(AddAuditorReq addAuditorReq);

    /**
     * 支付旁听订单
     * @param payConsultReq
     * @return
     */
    ResultRes<String> payAuditorOrder(PayConsultReq payConsultReq);

    /**
     * 修改咨询状态
     * @param orderUuid
     * @param orderType
     * @param checkSts
     */
    void updateConsultCheckSts(String orderUuid, Integer orderType,Integer checkSts);

    /**
     * 查询咨询列表
     * @param param
     * @return
     */
    PageRes<List<ConsultInfoListRes>> queryConsultList(QueryConsultListReq param);


    /**
     * 查询咨询订单列表
     * @param param
     * @return
     */
    PageRes<List<OrderConsultInfoListRes>> queryOrderConsultList(QueryOrderConsultListReq param);

    /**
     * 查询咨询订单详情
     * @param uuid
     * @return
     */
    ResultRes<ConsultOrderDetailRes> queryOrderConsultDetail(String uuid);

    /**
     * 咨询订单信息导出
     * @param exportReq
     * @param response
     */
    void exportOrderConsultList(QueryOrderConsultListReq exportReq, HttpServletResponse response);

    /**
     * 根据咨询ID查询咨询信息
     * @param consultUuid
     * @return
     */
    Consult checkConsult(String consultUuid);

    /**
     * 全国咨询订单枪单（技师）
     * @return
     */
    ResultRes<String> consultOrderSnapUp(String uuid);
}
