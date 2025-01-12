package com.car.account.web.service.vehicle;

import com.car.account.client.request.vehicle.AddVehicleTestingReq;
import com.car.account.client.response.vehicle.vehicleUser.QueryVehicleTesting;
import com.car.common.res.ResultRes;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-08 19:15
 */
public interface VehicleTestingService {
    ResultRes<String> addVehicleTesting(AddVehicleTestingReq addVehicleTestingRes);

    ResultRes<QueryVehicleTesting> queryVehicleTesting();
}
