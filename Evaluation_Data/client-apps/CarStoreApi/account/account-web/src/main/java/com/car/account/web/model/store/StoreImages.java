package com.car.account.web.model.store;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@Table(name = "store_images")
public class StoreImages extends BaseModelInfo {

    /**
     * 店铺uuid
     */
    @Column(name = "store_uuid")
    private String storeUuid;

    /**
     * 图片类型:1 营业执照  2 门店图片
     */
    @Column(name = "image_type")
    private Integer imageType;

    /**
     * 图片地址
     */
    @Column(name = "image_url")
    private String imageUrl;

}
