package com.car.order.web.service.maintain;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.dtc.AddDtcReq;
import com.car.order.client.request.dtc.CheckDtcReq;
import com.car.order.client.request.dtc.QueryDtcListReq;
import com.car.order.client.request.maintain.AddMaintainReq;
import com.car.order.client.request.maintain.QueryMaintainListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.client.response.maintain.QueryMaintainRes;
import com.car.order.web.model.maintain.Maintain;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 19:13
 */
public interface MaintainService {

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    ResultRes<QueryMaintainRes> getById(String uuid);


    /**
     * 新增养护信息
     * @param req
     * @return
     */
    ResultRes<String> add(AddMaintainReq req);


    /**
     * 根据id修改dtc信息
     * @param dtc
     * @param uuid
     * @return
     */
    ResultRes<String> updateById(AddMaintainReq dtc, String uuid);


    /**
     * 删除dtc信息
     * @param uuid
     * @return
     */
    ResultRes<String> deleteById(String uuid);

    /**
     * 分页列表
     * @param req
     * @return
     */
    PageRes<List<QueryMaintainRes>> list(@RequestBody QueryMaintainListReq req);



}
