package com.car.order.web.model.goods;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Data
@Table(name = "goods_images")
public class GoodsImages extends BaseModelInfo {

    /**
     * 商品uuid
     */
    @Column(name = "goods_uuid")
    private String goodsUuid;

    /**
     * 图片地址
     */
    @Column(name = "img_path")
    private String imgPath;

    /**
     * 图片类型:0 主图 1 其他
     */
    @Column(name = "img_type")
    private Integer imgType;
}
