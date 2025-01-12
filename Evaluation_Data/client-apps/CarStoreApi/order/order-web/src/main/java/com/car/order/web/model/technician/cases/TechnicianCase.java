package com.car.order.web.model.technician.cases;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 技师案例
 *
 * @author zhangyp
 * @createDate 2021-01-21
 */
@Data
@Table(name = "technician_case")
@EqualsAndHashCode(callSuper = true)
public class TechnicianCase extends BaseModelInfo {
    /**
     * 技师uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 行驶里程单位km
     */
    @Column(name = "mileage")
    private Integer mileage;

    /**
     * 制造日期yyyy/MM/dd
     */
    @Column(name = "made_time")
    private String madeTime;

    /**
     * 车架号
     */
    @Column(name = "vin")
    private String vin;

    /**
     * 所属系统字典表attach_sys
     */
    @Column(name = "attach_sys")
    private String attachSys;

    /**
     * 金额
     */
    @Column(name = "amt")
    private BigDecimal amt;

    /**
     * 品牌字典表repair_brand
     */
    @Column(name = "brand_uuid")
    private String brandUuid;

    /**
     * 型号
     */
    @Column(name = "model")
    private String model;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 动力信息
     */
    @Column(name = "power_info")
    private String powerInfo;

    /**
     * 故障现象
     */
    @Column(name = "fault_desc")
    private String faultDesc;

    /**
     * 诊断思路和过程
     */
    @Column(name = "idea_process")
    private String ideaProcess;

    /**
     * 结论总结
     */
    @Column(name = "summary")
    private String summary;

    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "check_status")
    private Integer checkStatus;

    /**
     * 审核人
     */
    @Column(name = "checkor")
    private String checkor;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}