package com.car.order.web.service.sence;


import com.car.common.res.ResultRes;
import com.car.order.client.request.scene.AddSceneTechnicianInfoReq;

/**
 * 现场订单技师相关
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2021-02-26 22:08:47
 */
public interface SceneOrderTechnicianService {

    /**
     * 新增现场订单技师内容
     * @param req
     * @return
     */
    ResultRes<String> addSceneTechnicianInfo(AddSceneTechnicianInfoReq req);

}

