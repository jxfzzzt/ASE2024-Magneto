package com.car.order.client.response.scene;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/5
 */
@Data
@ApiModel(value = "QuerySceneOrderInfoRes", description = "查询现场订单详情VO")
public class QuerySceneOrderInfoRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号", name = "orderNum")
    private String orderNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付", name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "发布者姓名", name = "issuerName")
    private String issuerName;

    @ApiModelProperty(value = "发布者手机号码", name = "issuerMobile")
    private String issuerMobile;

    @ApiModelProperty(value = "购买者姓名", name = "buyerName")
    private String buyerName;

    @ApiModelProperty(value = "购买者手机号码", name = "buyerMobile")
    private String buyerMobile;

    @ApiModelProperty(value = "品牌名称", name = "brandName")
    private String brandName;

    @ApiModelProperty(value = "车型uuid", name = "carModelUuid")
    private String carModelUuid;

    @ApiModelProperty(value = "车型名称", name = "carModelName")
    private String carModelName;

    @ApiModelProperty(value = "车款", name = "carStyle")
    private String carStyle;

    @ApiModelProperty(value = "VIN码", name = "vinCode")
    private String vinCode;

    @ApiModelProperty(value = "变速器一级", name = "transmissionOneLevel")
    private String transmissionOneLevel;

    @ApiModelProperty(value = "变速器一级uuid(对应字典uuid)", name = "transmissionOneLevelUuid")
    private String transmissionOneLevelUuid;

    @ApiModelProperty(value = "变速器二级", name = "transmissionTwoLevel")
    private String transmissionTwoLevel;

    @ApiModelProperty(value = "变速器二级（对应字典uuid）", name = "transmissionTwoLevelUuid")
    private String transmissionTwoLevelUuid;

    @ApiModelProperty(value = "发动机排量", name = "engineDisplacement")
    private String engineDisplacement;

    @ApiModelProperty(value = "发动机排量uuid(对应字典uuid)", name = "engineDisplacementUuid")
    private String engineDisplacementUuid;

    @ApiModelProperty(value = "驱动方式", name = "drivingMode")
    private String drivingMode;

    @ApiModelProperty(value = "驱动方式Uuid(对应字典uuid)", name = "drivingModeUuid")
    private String drivingModeUuid;

    @ApiModelProperty(value = "增压系统", name = "boosterSystem")
    private String boosterSystem;

    @ApiModelProperty(value = "增压系统uuid(对应字典uuid)", name = "boosterSystemUuid")
    private String boosterSystemUuid;

    @ApiModelProperty(value = "保修状态，0保修，1不保修", name = "warrantySts")
    private Integer warrantySts;

    @ApiModelProperty(value = "其他状态", name = "otherSts")
    private String otherSts;

    @ApiModelProperty(value = "故障描述", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "维修类型", name = "repairType")
    private String repairType;

    @ApiModelProperty(value = "维修类型uuid（对应字典表uuid）", name = "repairTypeUuid")
    private String repairTypeUuid;

    @ApiModelProperty(value = "已检过程", name = "alreadyInspect")
    private String alreadyInspect;

    @ApiModelProperty(value = "DTC故障code", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "基本检查费用", name = "basicInspectAmount")
    private BigDecimal basicInspectAmount;

    @ApiModelProperty(value = "基本检查费用Uuid", name = "basicInspectAmountUuid")
    private String basicInspectAmountUuid;

    @ApiModelProperty(value = "相关线路检查费用", name = "lineInspectAmount")
    private BigDecimal lineInspectAmount;

    @ApiModelProperty(value = "相关线路检查费用Uuid", name = "lineInspectAmountUuid")
    private String lineInspectAmountUuid;

    @ApiModelProperty(value = "诊断仪使用费", name = "diagnosisInstrumentAmount")
    private BigDecimal diagnosisInstrumentAmount;

    @ApiModelProperty(value = "诊断仪使用费Uuid", name = "diagnosisInstrumentAmountUuid")
    private String diagnosisInstrumentAmountUuid;

    @ApiModelProperty(value = "车辆钣金修复费用", name = "carSheetMetalAmount")
    private BigDecimal carSheetMetalAmount;

    @ApiModelProperty(value = "车辆钣金修复费用Uuid", name = "carSheetMetalAmountUuid")
    private String carSheetMetalAmountUuid;

    @ApiModelProperty(value = "车辆油漆修复费用", name = "carPaintRepairAmount")
    private BigDecimal carPaintRepairAmount;

    @ApiModelProperty(value = "车辆油漆修复费用Uuid", name = "carPaintRepairAmountUuid")
    private String carPaintRepairAmountUuid;

    @ApiModelProperty(value = "其他费用费", name = "otherAmount")
    private BigDecimal otherAmount;

    @ApiModelProperty(value = "其他费用费Uuid", name = "otherAmountUuid")
    private String otherAmountUuid;

    @ApiModelProperty(value = "平台订单服务费", name = "orderServiceAmount")
    private BigDecimal orderServiceAmount;

    @ApiModelProperty(value = "平台订单服务费Uuid", name = "orderServiceAmountUuid")
    private String orderServiceAmountUuid;

    @ApiModelProperty(value = "总支付费用", name = "totalAmount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "dtc图片", name = "dtcImageList")
    private List<String> dtcImageList;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败 6：已完成", name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "是否是本人发布，true：是， false：否", name = "isOneself")
    private Boolean isOneself;

    @ApiModelProperty(value = "抢单状态 0未抢，1已抢", name = "grabbingOrdersSts")
    private Integer grabbingOrdersSts;

    @ApiModelProperty(value = "现场下单技师信息uuid")
    private String sceneOrderTechnicianUuid;

    @ApiModelProperty(value = "技师信息故障描述", name = "faultDesc")
    private String technicianFaultDesc;

    @ApiModelProperty(value = "技师信息已检过程", name = "alreadyInspect")
    private String technicianAlreadyInspect;

    @ApiModelProperty(value = "技师信息DTC故障code", name = "dtcCode")
    private String technicianDtcCode;

    @ApiModelProperty(value = "技师信息维修总结", name = "repairSummary")
    private String repairSummary;

    @ApiModelProperty(value = "技师信息故障是否解决 0解决，1未解决", name = "faultSolve")
    private Integer faultSolve;

    @ApiModelProperty(value = "技师信息Dtc故障图片", name = "dtcImgList")
    private List<String> technicianDtcImgList;

}
