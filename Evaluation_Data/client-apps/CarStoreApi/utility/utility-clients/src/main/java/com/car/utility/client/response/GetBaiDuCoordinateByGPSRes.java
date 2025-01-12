package com.car.utility.client.response;

import lombok.Data;

import java.util.List;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.cutover.cutover.utils
 * @NAME: HttpResult
 * @DATE: 2021/3/4 11:05
 */
@Data
public class GetBaiDuCoordinateByGPSRes {

    /**
     * 返回结果状态值， 成功返回0
     */
    private Integer status;

    /**
     * 解析对象
     */
    private List<BaiDuPositionRes> result;

    /**
     * 内容描述
     */
    private String msg;
}
