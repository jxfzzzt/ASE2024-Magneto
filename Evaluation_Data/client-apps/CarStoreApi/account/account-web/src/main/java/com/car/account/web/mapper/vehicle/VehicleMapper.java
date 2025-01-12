package com.car.account.web.mapper.vehicle;

import com.car.account.web.model.vehicle.Vehicle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @author xlj
 */
@Repository
public interface VehicleMapper extends Mapper<Vehicle> {

    /**
     * 查询当前用户名下车辆信息
     * @param userUuid
     * @return
     */
    List<Vehicle> queryListByUser(@Param("userUuid") String userUuid);


    /**
     * 查询我的车辆数量
     * @param userUuid
     * @return
     */
    Integer queryVehicleCount(@Param("userUuid") String userUuid);
}
