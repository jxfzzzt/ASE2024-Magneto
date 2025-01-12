package com.car.order.client.request.scene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/27
 */
@Data
@ApiModel(value = "addSceneOrderReq", description = "新增现场下单订单VO")
public class AddSceneOrderReq {

    @ApiModelProperty(value = "dtc故障uuid", name = "dtcUuid")
    private String dtcUuid;

    @ApiModelProperty(value = "品牌uuid", name = "brandUuid")
    @NotBlank(message = "请选择车辆品牌！")
    private String brandUuid;

    @ApiModelProperty(value = "车型uuid", name = "carModelUuid")
    @NotBlank(message = "请选择车型！")
    private String carModelUuid;

    @ApiModelProperty(value = "车款", name = "carStyle")
    @NotBlank(message = "请选择车款！")
    private String carStyle;

    @ApiModelProperty(value = "VIN码", name = "vinCode")
    private String vinCode;

    @ApiModelProperty(value = "变速器一级uuid(对应字典uuid)", name = "transmissionOneLevelUuid")
    private String transmissionOneLevelUuid;

    @ApiModelProperty(value = "变速器二级（对应字典uuid）", name = "transmissionTwoLevelUuid")
    private String transmissionTwoLevelUuid;

    @ApiModelProperty(value = "发动机排量uuid(对应字典uuid)", name = "engineDisplacementUuid")
    private String engineDisplacementUuid;

    @ApiModelProperty(value = "驱动方式Uuid(对应字典uuid)", name = "drivingModeUuid")
    private String drivingModeUuid;

    @ApiModelProperty(value = "增压系统uuid(对应字典uuid)", name = "boosterSystemUuid")
    private String boosterSystemUuid;

    @ApiModelProperty(value = "保修状态0保修，1不保修", name = "warrantySts")
    private Integer warrantySts;

    @ApiModelProperty(value = "其他状态", name = "otherSts")
    private String otherSts;

    @ApiModelProperty(value = "故障描述", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "维修类型uuid（对应字典表uuid）", name = "repairTypeUuid")
    private String repairTypeUuid;

    @ApiModelProperty(value = "已检过程", name = "alreadyInspect")
    private String alreadyInspect;

    @ApiModelProperty(value = "DTC故障code", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "基本上门费用Uuid", name = "basicDoorAmountUuid")
    @NotBlank(message = "请选择基本上门费用！")
    private String basicDoorAmountUuid;


    @ApiModelProperty(value = "平台订单服务费Uuid", name = "orderServiceAmountUuid")
    private String orderServiceAmountUuid;

    @ApiModelProperty(value = "总费用", name = "totalAmount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "详细地址", name = "detailedAddr")
    private String detailedAddr;

    @ApiModelProperty(value = "dtc图片", name = "dtcImageList")
    private List<String> dtcImageList;

    @ApiModelProperty(value = "故障描述图片", name = "dtcImageList")
    private List<String> faultDescImageList;

    @ApiModelProperty(value = "服务时间", name = "dtcImageList")
    private String serviceDate ;
}
