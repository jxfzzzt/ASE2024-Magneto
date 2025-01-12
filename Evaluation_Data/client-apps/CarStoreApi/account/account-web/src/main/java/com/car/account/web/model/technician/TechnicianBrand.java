package com.car.account.web.model.technician;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 技师账户信息
 * @author xlj
 * @date 2020/12/19
 */
@Data
@Table(name = "technician_brand")
public class TechnicianBrand extends BaseModelInfo {

    /**
     * 技师ID
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 品牌，对应字典表
     */
    @Column(name = "brand_uuid")
    private String brandUuid;





}
