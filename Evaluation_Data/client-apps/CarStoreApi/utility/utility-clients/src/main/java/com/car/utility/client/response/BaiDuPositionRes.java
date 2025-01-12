package com.car.utility.client.response;

import lombok.Data;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.cutover.cutover.utils
 * @NAME: BaiduLatitudeLongitudeLocation
 * @DATE: 2021/3/4 10:45
 */
@Data
public class BaiDuPositionRes {

    /**
     * 纬度值
     */
    private Float x;

    /**
     * 经度值
     */
    private Float y;
}
