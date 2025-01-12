package com.car.account.web.service.technician;

import com.car.account.client.request.technician.AddAndUpdateTechnicianAccountReq;
import com.car.account.client.request.technician.UpdateTechnicianAccountReq;
import com.car.account.client.response.technician.TechnicianAccountRes;
import com.car.common.res.ResultRes;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/1
 */
public interface TechnicianAccountService {

    /**
     * 查询技师账户信息
     * @return
     */
    ResultRes<TechnicianAccountRes> queryTechnicianAccountInfo();

    /**
     * 新增/修改技师账户信息
     * @param req
     * @return
     */
    ResultRes<String> addAndUpdateTechnicianAccount(AddAndUpdateTechnicianAccountReq req);

    /**
     * 修改技师账户信息
     * @param req
     * @return
     */
    ResultRes<String> updateTechnicianAccount (UpdateTechnicianAccountReq req);
}
