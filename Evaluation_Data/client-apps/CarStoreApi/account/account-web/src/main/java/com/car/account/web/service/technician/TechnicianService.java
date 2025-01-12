package com.car.account.web.service.technician;

import com.car.account.client.request.technician.TechnicianAnswerReq;
import com.car.account.client.request.technician.TechnicianListReq;
import com.car.account.client.request.technician.TechnicianLocationListReq;
import com.car.account.client.request.technician.TechnicianReq;
import com.car.account.client.response.technician.*;
import com.car.account.client.response.technician.caset.CaseStatisticsRes;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xlj
 */
public interface TechnicianService {


    /**
     * 注册技师
     * @param params
     * @return
     */
    ResultRes<String> addTechnician(TechnicianReq params);

    /**
     * 删除技师
     * @param uuid
     * @return
     */
    ResultRes<String> deleteTechnician(String uuid);

    /**
     * 查询技师统计数据（注册/订单/案例/回答/支持）
     * @param param
     * @return
     */
    ResultRes<TechnicianCountRes> queryTechnicianCount(TechnicianListReq param);

    /**
     * 查询技师信息列表
     * @param param
     * @return
     */
    PageRes<List<TechnicianListRes>> queryTechnicianList(TechnicianListReq param);

    /**
     * 技师信息导出
     * @param exportReq
     * @param response
     */
    void exportTechnicianList(TechnicianListReq exportReq, HttpServletResponse response);

    /**
     * 查询技师详情
     * @param uuid
     * @return
     */
    ResultRes<TechnicianRes> queryTechnicianDetail(String uuid);

    /**
     * 修改技师信息
     * @param param
     * @return
     */
    ResultRes updateTechnician(TechnicianReq param);

    /**
     * 查询技师问答列表
     * @param params
     * @return
     */
    PageRes<List<TechnicianAnswerRes>> queryTechnicianAnswerList(TechnicianAnswerReq params);

    /**
     * 查询技师案例列表
     * @param pageReq
     * @return
     */
    PageRes<List<CaseStatisticsRes>> queryCaseStatisticsList(PageReq pageReq);


    /**
     * 查询布点技师信息列表
     * @param req
     * @return
     */
    ResultRes<List<TechnicianLocationListRes>> queryTechnicianLocationList(TechnicianLocationListReq req);

    /**
     * 修改技师问答数量
     * @param uuid
     * @return
     */
    ResultRes<String> updateQaCount(String uuid);
}
