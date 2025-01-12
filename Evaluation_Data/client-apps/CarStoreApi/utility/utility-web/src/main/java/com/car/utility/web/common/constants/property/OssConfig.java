package com.car.utility.web.common.constants.property;

import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/11 13:17
 */
@Data
public class OssConfig {
    private String accessKey;
    private String secretKey;
    private String endPoint;
    private String bucketName;
}
