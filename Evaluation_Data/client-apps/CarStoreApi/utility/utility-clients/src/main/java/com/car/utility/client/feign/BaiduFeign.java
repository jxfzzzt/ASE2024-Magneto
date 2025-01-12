package com.car.utility.client.feign;

import com.car.common.constant.ServiceNameConstant;
import com.car.common.res.ResultRes;
import com.car.utility.client.response.GetBaiDuCoordinateByGPSRes;
import com.car.utility.client.response.LocationResultRes;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.utility.client.feign
 * @NAME: BaiduFeign
 * @DATE: 2021/3/4 20:55
 */
@FeignClient(value = ServiceNameConstant.UTILITY)
public interface BaiduFeign {

    /**
     * 百度地址信息转经纬度
     * @return
     */
    @GetMapping(value = "/baidu/getAddressLatitudeLongitude/{address}")
    ResultRes<LocationResultRes> getAddressLatitudeLongitude(@PathVariable(name = "address")String address);

    /**
     * 根据GPS转化百度坐标
     * @param coords  121.226967,31.107241
     * @return
     */
    @GetMapping(value = "/baidu/getBaiDuCoordinateByGPS/{coords}")
    public ResultRes<GetBaiDuCoordinateByGPSRes> getBaiDuCoordinateByGPS(@PathVariable(name = "coords") String coords);
}
