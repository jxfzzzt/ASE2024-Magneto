package com.car.utility.web.service;

import com.car.common.res.ResultRes;
import com.car.utility.client.response.LocationResultRes;
import com.car.utility.client.response.GetBaiDuCoordinateByGPSRes;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.utility.web.service
 * @NAME: BaiduService
 * @DATE: 2021/3/4 20:47
 */
public interface BaiduService {

    /**
     * 根据地址查询经纬度
     * @param address
     * @return
     */
    public ResultRes<LocationResultRes> getAddressLatitudeLongitude(String address);

    ResultRes<GetBaiDuCoordinateByGPSRes> getBaiDuCoordinateByGPS(String coords);
}
