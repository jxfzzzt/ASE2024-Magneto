package com.car.order.client.response.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="ConsultOrderFrontRes",description="案例详情")
public class CaseDetails {

    @ApiModelProperty(value = "技师名称", name = "technicianName")
    private String userName;

    @ApiModelProperty(value = "技师工龄", name = "technicianImgUrl")
    private String workingYear;

    @ApiModelProperty(value = "发布时间", name = "technicianImgUrl")
    private String createdTime;

    @ApiModelProperty(value = "标题",name = "title")
    private String title;

    @ApiModelProperty(value = "品牌", name = "technicianImgUrl")
    private String orderName;

    @ApiModelProperty(value = "型号", name = "technicianImgUrl")
    private String model;

    @ApiModelProperty(value = "动力信息", name = "technicianImgUrl")
    private String powerInfo;

    @ApiModelProperty(value = "行驶里程", name = "technicianImgUrl")
    private String mileage;

    @ApiModelProperty(value = "车辆执照年月", name = "technicianImgUrl")
    private String madeTime;

    @ApiModelProperty(value = "vin", name = "technicianImgUrl")
    private String vin;

    @ApiModelProperty(value = "故障现象", name = "technicianImgUrl")
    private String faultDesc;

    @ApiModelProperty(value = "诊断思路和过程", name = "technicianImgUrl")
    private String ideaProcess;

    @ApiModelProperty(value = "价格", name = "technicianImgUrl")
    private String amt;

    @ApiModelProperty(value = "评分", name = "technicianImgUrl")
    private String score;

    @ApiModelProperty(value = "购买次数", name = "technicianImgUrl")
    private String purchaseNumber;

    @ApiModelProperty(value = "是否是本人", name = "technicianImgUrl")
    private String belongTo ;
}
