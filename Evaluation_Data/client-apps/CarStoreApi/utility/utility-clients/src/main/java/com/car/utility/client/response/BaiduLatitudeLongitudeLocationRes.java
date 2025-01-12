package com.car.utility.client.response;

import lombok.Data;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.cutover.cutover.utils
 * @NAME: BaiduLatitudeLongitudeLocation
 * @DATE: 2021/3/4 10:45
 */
@Data
public class BaiduLatitudeLongitudeLocationRes {

    /**
     * 纬度值
     */
    private Float lat;

    /**
     * 经度值
     */
    private Float lng;
}
