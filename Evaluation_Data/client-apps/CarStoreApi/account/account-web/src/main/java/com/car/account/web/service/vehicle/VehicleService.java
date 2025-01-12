package com.car.account.web.service.vehicle;

import com.car.account.client.request.vehicle.VehicleReq;
import com.car.account.client.response.consult.ConsultRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleRes;
import com.car.common.req.PageReq;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import java.util.List;

/**
 * @author xlj
 */
public interface VehicleService {

    /**
     * 查询我的车辆列表
     * @param
     * @return
     */
    ResultRes<List<VehicleRes>> queryMyVehicle();

    /**
     * 添加用户车辆信息
     * @param param
     * @return
     */
    ResultRes<String> addMyVehicle(VehicleReq param);

    /**
     * 检查车牌号是否重复
     * @param vehicleUuid
     * @param plateNumber
     * @return
     */
    boolean checkPlateNumber(String vehicleUuid, String plateNumber);

    /**
     * 编辑用户车辆信息
     * @param param
     * @return
     */
    ResultRes<String> editMyVehicle(VehicleReq param);

    /**
     * 删除我的车辆信息
     * @param vehicleUuid
     * @return
     */
    ResultRes<String> deleteMyVehicle(String vehicleUuid);

    /**
     * 问题列表 > 可咨询
     * @param pageReq
     * @return
     */
    PageRes<List<ConsultRes>>  queryConsultList(PageReq pageReq);

    /**
     * 查询我的车辆详情
     * @param vehicleUuid
     * @return
     */
    ResultRes<VehicleRes> queryVehicleDetail(String vehicleUuid);
}
