package com.car.utility.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.res.ResultRes;
import com.car.utility.client.response.LocationResultRes;
import com.car.utility.client.response.GetBaiDuCoordinateByGPSRes;
import com.car.utility.web.common.utils.HttpUtils;
import com.car.utility.web.service.BaiduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.cutover.cutover.utils
 * @NAME: BaiduUtils
 * @DATE: 2021/3/4 10:34
 */
@Slf4j
@Service
public class BaiduServiceImpl implements BaiduService {

    @Value("${baidu.api}")
    private String baiduApi;

    @Value("${baidu.positionApi}")
    private String positionApi;

    @Value("${baidu.AK}")
    private String baiduAK;

    @Override
    public ResultRes<LocationResultRes> getAddressLatitudeLongitude(String address) {
        //组装查询经纬度参数入参
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", address);
        paramMap.put("output", "json");
        paramMap.put("ak", baiduAK);
        String body = "";
        LocationResultRes res = null;
        try {
            body = HttpUtils.sendGet(baiduApi, paramMap, null);
            res = JSON.parseObject(body, LocationResultRes.class);
            if(Integer.valueOf(1).equals(res.getStatus())){
                return ResultRes.error(ResEnum.SYSTEM_ERROR.getValue(),res.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultRes.success(res);
    }

    /**
     *
     * @param coords
     * @return
     */
    @Override
    public ResultRes<GetBaiDuCoordinateByGPSRes> getBaiDuCoordinateByGPS(String coords) {
        //组装查询经纬度参数入参
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("coords", coords);
        paramMap.put("from", "1");
        paramMap.put("to", "5");
        paramMap.put("output", "json");
        paramMap.put("ak", baiduAK);
        String body = "";
        GetBaiDuCoordinateByGPSRes res = null;
        try {
            body = HttpUtils.sendGet(positionApi, paramMap, null);
            res = JSON.parseObject(body, GetBaiDuCoordinateByGPSRes.class);
            if(Integer.valueOf(1).equals(res.getStatus())){
                return ResultRes.error(ResEnum.SYSTEM_ERROR.getValue(),res.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultRes.success(res);
    }
}
