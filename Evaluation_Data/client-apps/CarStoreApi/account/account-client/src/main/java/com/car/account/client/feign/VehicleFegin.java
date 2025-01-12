package com.car.account.client.feign;

import com.car.account.client.response.vehicle.config.ConfigRes;
import com.car.account.client.response.vehicle.vehicleUser.VehicleUserRes;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangyp
 * @date 2021/1/21 21:54
 */
@FeignClient(value = "account")
public interface VehicleFegin {

    @RequestMapping(value = "/vehicleUser/queryDetail/{uuid}", method = RequestMethod.GET)
    public ResultRes<VehicleUserRes> queryDetail(@PathVariable("uuid") String uuid);


    /**
     * 根据uuid查询车辆节点信息
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/vehicleConfig/queryConfig/{uuid}", method = RequestMethod.GET)
    ResultRes<ConfigRes> queryConfig(@PathVariable(name = "uuid") String uuid);
}
