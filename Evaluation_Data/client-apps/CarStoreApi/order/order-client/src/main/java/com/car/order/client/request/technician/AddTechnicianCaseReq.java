package com.car.order.client.request.technician;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/21 0:17
 */
@Data
@ApiModel
public class AddTechnicianCaseReq {

    /**
     * 行驶里程单位km
     */
    @NotNull(message = "请填写行驶里程！")
    @ApiModelProperty(value="行驶里程单位km",name="mileage")
    private Integer mileage;

    /**
     * 制造日期yyyy/MM/dd
     */
    @ApiModelProperty(value = "开始时间 yyyy-MM-dd",name="startDate",example = "2020-12-30")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotBlank(message = "请填写制造日期！")
    private String madeTime;

    /**
     * 车架号
     */
    @ApiModelProperty(value="车架号",name="vin")
    private String vin;

    /**
     * 所属系统字典表attach_sys
     */
    @ApiModelProperty(value="所属系统字典表attach_sys",name="attachSys")
    @NotBlank(message = "请填写所属系统！")
    private String attachSys;

    /**
     * 金额
     */
    @NotNull(message = "请填写案例金额！")
    @ApiModelProperty(value="金额",name="amt")
    private BigDecimal amt;

    /**
     * 品牌字典表repair_brand
     */
    @ApiModelProperty(value="品牌字典表repair_brand",name="brandUuid")
    @NotBlank(message = "请填写案例车辆品牌！")
    private String brandUuid;

    /**
     * 型号
     */
    @ApiModelProperty(value="车型uuid,account模块车辆管理配置获取",name="model")
    @NotBlank(message = "请填写案例车辆型号！")
    private String model;

    /**
     * 标题
     */
    @ApiModelProperty(value="标题",name="title")
    @NotBlank(message = "请填写案例标题！")
    private String title;

    /**
     * 动力信息
     */
    @ApiModelProperty(value="动力信息:发动机类型+变速器信息 手动输入",name="powerInfo")
    @NotBlank(message = "请填写动力信息！")
    private String powerInfo;

    /**
     * 故障现象
     */
    @ApiModelProperty(value="故障现象",name="faultDesc")
    @NotBlank(message = "请填写故障现象！")
    private String faultDesc;

    /**
     * 诊断思路和过程
     */
    @ApiModelProperty(value="诊断思路和过程",name="ideaProcess")

    private String ideaProcess;

    /**
     * 结论总结
     */
    @ApiModelProperty(value="结论总结",name="summary")
    private String summary;

    /**
     * 案例资源列表
     */
    @ApiModelProperty(value="案例资源列表 图片/文件",name="caseImgList")
    private List<String> caseImgList;
}
