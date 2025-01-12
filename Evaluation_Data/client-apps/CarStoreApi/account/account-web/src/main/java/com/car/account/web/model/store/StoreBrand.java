package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/1/20
 */
@Data
@Table(name = "store_brand")
public class StoreBrand extends BaseModelInfo {

    /**
     * 汽车品牌主键
     */
    @Column(name = "brand_uuid")
    private String brandUuid;

    /**
     * 店铺主键
     */
    @Column(name = "store_uuid")
    private String storeUuid;
}
