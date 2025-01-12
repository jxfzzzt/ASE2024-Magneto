package com.car.order.web.model.driving;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@Table(name = "after_sale_images")
public class AfterSaleImages extends BaseModelInfo {


    /**
     * 订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private BigDecimal imgUrl;

}
