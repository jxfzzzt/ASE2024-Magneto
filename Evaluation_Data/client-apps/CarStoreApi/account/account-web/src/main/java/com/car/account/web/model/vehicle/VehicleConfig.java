package com.car.account.web.model.vehicle;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author xlj
 * @date 2020/12/19
 */
@Data
@Table(name = "vehicle_config")
public class VehicleConfig extends BaseModelInfo {

    /**
     * 配置名称
     */
    @Column(name = "config_name")
    private String configName;

    /**
     * 父类编码，根节点默认：-1
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 配置类型 1：车辆类型 2：车辆品牌 3：车辆型号
     */
    @Column(name = "config_type")
    private String configType;

    /**
     * 排序
     */
    @Column(name = "sort_num")
    private Integer sortNum;

}
