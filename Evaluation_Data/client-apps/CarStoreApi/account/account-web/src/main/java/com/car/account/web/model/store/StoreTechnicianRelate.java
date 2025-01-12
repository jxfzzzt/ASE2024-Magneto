package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Table(name = "store_technician_relate")
@Data
public class StoreTechnicianRelate extends BaseModelInfo {

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 技师uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 审核状态
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

}
