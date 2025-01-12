package com.car.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Buerger
 * @date 2020/9/24 19:04
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {


    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "";

    /**
     * swagger会解析的包路径
     **/
    private String pathMapping = "";

    /**
     * 标题
     **/
    private String title = "";
    /**
     * 描述
     **/
    private String description = "";
    /**
     * 版本
     **/
    private String version = "";
    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";

    /**
     * host信息
     **/
    private String host = "";



}
