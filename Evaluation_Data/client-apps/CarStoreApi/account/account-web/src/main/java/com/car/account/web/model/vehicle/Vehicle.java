package com.car.account.web.model.vehicle;

import com.car.common.datasource.model.BaseModelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@Table(name = "vehicle")
public class Vehicle extends BaseModelInfo {

    /**
     * 车主用户ID
     */
    @Column(name = "vehicle_user_uuid")
    private String vehicleUserUuid;

    /**
     * 车牌号
     */
    @Column(name = "plate_number")
    private String plateNumber;

    /**
     * VIN编码
     */
    @Column(name = "vin_code")
    private String vinCode;

    /**
     * 发动机型号
     */
    @Column(name = "engine_type")
    private String engineType;

    /**
     * 行驶证编号
     */
    @Column(name = "license_number")
    private String licenseNumber;

    /**
     * 行驶证照片
     */
    @Column(name = "license_image_url")
    private String licenseImageUrl;

    /**
     * 行驶证登记日期
     */
    @Column(name = "license_register_date")
    private String licenseRegisterDate;

    /**
     * 购买日期
     */
    @Column(name = "buy_date")
    private String buyDate;

    /**
     * 车辆排放等级,对应字典表code
     */
    @Column(name = "emission_levels")
    private String emissionLevels;

    /**
     * 排量,对应字典表code
     */
    @Column(name = "displacement")
    private String displacement;

    /**
     * 车辆类型,对应字典表uuid
     */
    @Column(name = "vehicle_type")
    private String vehicleType;

    /**
     * 车辆品牌
     */
    @Column(name = "vehicle_brand")
    private String vehicleBrand;

    /**
     * 车型
     */
    @Column(name = "vehicle_model")
    private String vehicleModel;

    /**
     * 燃油类型
     */
    @Column(name = "fuel_type")
    private String fuelType;

    /**
     * 后处理系统
     */
    @Column(name = "after_treatment_system")
    private String afterTreatmentSystem;
    /**
     * 车辆排放等级名称
     */
    @Transient
    private String emissionLevelsName;
    /**
     * 排量名称
     */
    @Transient
    private String displacementName;
    /**
     * 车辆类型名称
     */
    @Transient
    private String vehicleTypeName;
    /**
     * 车辆品牌名称
     */
    @Transient
    private String vehicleBrandName;
    /**
     * 车型名称
     */
    @Transient
    private String vehicleModelName;
    /**
     * 燃油类型名称
     */
    @Transient
    private String fuelTypeName;
}
