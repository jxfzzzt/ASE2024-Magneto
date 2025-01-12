package com.car.account.web.model.technician;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 技师店铺历史记录表
 * @author xlj
 * @date 2020/12/19
 */
@Data
@Table(name = "technician_store_history")
public class TechnicianStoreHistory extends BaseModelInfo {

    /**
     * 技师ID
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 店铺ID
     */
    @Column(name = "store_uuid")
    private String storeUuid;





}
