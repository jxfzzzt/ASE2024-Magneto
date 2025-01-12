package com.car.account.web.model.vehicle;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 实体类
 * @since jdk1.8
 */
@Data
@Table(name = "vehicle_testing")
public class VehicleTesting extends BaseModelInfo {


    /**
     * 车主id
     */
    @Column(name = "vehicle_user_id")
     private String vehicleUserId ;

    /**
     * 技师id
     */
    @Column(name = "technician_uuid")
     private String technicianUuid ;

    /**
     * 意见
     */
    @Column(name = "remarks")
     private String remarks ;

    /**
     * 内容
     */
    @Column(name = "content")
     private String content ;






}