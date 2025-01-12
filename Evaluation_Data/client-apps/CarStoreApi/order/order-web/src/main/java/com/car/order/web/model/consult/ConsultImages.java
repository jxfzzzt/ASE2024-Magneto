package com.car.order.web.model.consult;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Data
@Table(name = "consult_images")
public class ConsultImages extends BaseModelInfo {

    /**
     * 咨询uuid
     */
    @Column(name = "consult_uuid")
    private String consultUuid;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 图片类型 0 咨询 1 回答
     */
    @Column(name = "img_type")
    private Integer imgType;
}
