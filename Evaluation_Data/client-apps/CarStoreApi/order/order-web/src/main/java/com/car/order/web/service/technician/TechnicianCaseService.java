package com.car.order.web.service.technician;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.technician.AddTechnicianCaseReq;
import com.car.order.client.request.technician.CaseForTechnicianListRep;
import com.car.order.client.request.technician.CaseForVehicleListRep;
import com.car.order.client.request.technician.UpdateTechnicianCaseReq;
import com.car.order.client.response.technician.CaseForTechnicianItemRes;
import com.car.order.client.response.technician.CaseForVehicleItemRes;
import com.car.order.client.response.technician.TechnicianCaseRes;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/21
 */
public interface TechnicianCaseService {

    /**
     * 新增技师案例
     * @param addTechnicianCaseReq
     * @return
     */
    ResultRes<String> addTechnicianCase(AddTechnicianCaseReq addTechnicianCaseReq);

    /**
     * 修改技师案例
     * @param updateTechnicianCaseReq
     * @return
     */
    ResultRes<String> updateTechnicianCase(UpdateTechnicianCaseReq updateTechnicianCaseReq);

    /**
     * 查询案例详情
     * @param uuid
     * @return
     */
    ResultRes<TechnicianCaseRes> queryTechnicianCaseDetail(String uuid);

    /**
     * 查询案例列表 技师查询
     * @param pageReq
     * @return
     */
    PageRes<List<CaseForTechnicianItemRes>> queryCaseForTechnicianList(CaseForTechnicianListRep pageReq);

    /**
     * 查询案例列表 车主查询
     * @param caseForVehicleListRep
     * @return
     */
    PageRes<List<CaseForVehicleItemRes>> queryCaseForVehicleList(CaseForVehicleListRep caseForVehicleListRep);

    /**
     * 修改技师案例状态
     * @param caseUuid
     * @param checkSts
     * @param checkor
     */
    void updateTechnicianCaseCheckSts(String caseUuid,Integer checkSts,String checkor);
}
