package com.car.account.web.service.vehicle;

import com.car.account.client.request.vehicle.VehicleUserListReq;
import com.car.account.client.request.vehicle.VehicleUserReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryOrderAmountRes;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleUserCountRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xlj
 */
public interface VehicleUserService {

    /**
     * 查询用户数（注册用户/车主数）
     * @return
     */
    ResultRes<QueryVehicleUserCountRes> queryVehicleUserCount();

    /**
     * 查询车主信息列表
     * @param req
     * @return
     */
    PageRes<List<VehicleUserRes>> queryList(VehicleUserListReq req);

    /**
     * 查询车主订单金额简要信息
     * @param uuid
     * @return
     */
    ResultRes<QueryOrderAmountRes> queryOrderAmount(String uuid);

    /**
     * 查询车主详情
     * @param uuid
     * @return
     */
    ResultRes<VehicleUserRes> queryDetail(String uuid);

    /**
     * 编辑车主信息
     * @param param
     * @return
     */
    ResultRes editVehicleUser(VehicleUserReq param);

    /**
     * 车主信息导出
     * @param exportReq
     * @param response
     */
    void exportVehicleList(VehicleUserListReq exportReq, HttpServletResponse response);

    /**
     * 根据登陆token获取车主详细信息
     * @return
     */
    ResultRes<VehicleUserRes> queryDetailByToken();
}
