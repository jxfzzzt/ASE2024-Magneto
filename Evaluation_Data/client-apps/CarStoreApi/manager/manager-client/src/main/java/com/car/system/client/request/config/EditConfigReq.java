package com.car.system.client.request.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author linjiang.xie
 * @date 2019/6/3 11:02
 */
@Data
public class EditConfigReq {

    @ApiModelProperty(value = "企业名称",name = "enterpriseName")
    private String enterpriseName;

    @ApiModelProperty(value = "管理员姓名",name = "manageName")
    private String manageName;

    @ApiModelProperty(value = "管理员电话",name = "managePhone")
    private String managePhone;

    @ApiModelProperty(value = "企业logo",name = "enterpriseLogoUrl")
    private String enterpriseLogoUrl;

    @ApiModelProperty(value = "企业logo小图",name = "enterpriseSmallLogoUrl")
    private String enterpriseSmallLogoUrl;

}
