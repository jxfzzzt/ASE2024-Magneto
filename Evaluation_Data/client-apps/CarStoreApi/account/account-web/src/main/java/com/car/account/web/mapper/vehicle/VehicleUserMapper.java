package com.car.account.web.mapper.vehicle;

import com.car.account.client.request.vehicle.VehicleUserListReq;
import com.car.account.web.model.vehicle.VehicleUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @author xlj
 */
@Repository
public interface VehicleUserMapper extends Mapper<VehicleUser> {

    /**
     * 查询车主信息列表
     * @param param
     * @return
     */
    List<VehicleUser> queryList(VehicleUserListReq param);
}
