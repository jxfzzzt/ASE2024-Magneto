package com.car.utility.web.controller;

import com.car.common.res.ResultRes;
import com.car.utility.client.response.LocationResultRes;
import com.car.utility.client.response.GetBaiDuCoordinateByGPSRes;
import com.car.utility.web.service.BaiduService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 百度相关接口
 *
 * @author zhoujian
 * @PACKAGE_NAME: com.car.utility.web.controller
 * @NAME: BaiduController
 * @DATE: 2021/3/4 20:31
 */
@RestController
@RequestMapping("/baidu")
@Slf4j
@Api(value = "BaiduController", tags = "百度相关接口")
public class BaiduController {

    @Autowired
    BaiduService baiduService;

    @GetMapping(value = "/getAddressLatitudeLongitude/{address}")
    @ApiOperation(value = "地址信息转经纬度", nickname = "getAddressLatitudeLongitude")
    public ResultRes<LocationResultRes> getAddressLatitudeLongitude(@PathVariable(name = "address") String address) {
        return baiduService.getAddressLatitudeLongitude(address);
    }

    @GetMapping(value = "/getBaiDuCoordinateByGPS/{coords}")
    @ApiOperation(value = "根据GPS转化百度坐标", nickname = "getBaiDuCoordinateByGPS")
    public ResultRes<GetBaiDuCoordinateByGPSRes> getBaiDuCoordinateByGPS(@PathVariable(name = "coords") String coords) {
        return baiduService.getBaiDuCoordinateByGPS(coords);
    }
}
