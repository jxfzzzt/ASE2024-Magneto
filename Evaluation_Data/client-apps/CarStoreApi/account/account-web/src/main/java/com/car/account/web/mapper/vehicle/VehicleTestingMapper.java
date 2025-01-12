package com.car.account.web.mapper.vehicle;

import com.car.account.web.model.vehicle.VehicleTesting;
import tk.mybatis.mapper.common.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTestingMapper extends Mapper<VehicleTesting>{

    public VehicleTesting queryVehicleTesting();
}