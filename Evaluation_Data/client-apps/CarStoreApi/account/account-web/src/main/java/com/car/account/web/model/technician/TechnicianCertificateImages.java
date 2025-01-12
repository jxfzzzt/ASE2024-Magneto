package com.car.account.web.model.technician;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/2/7
 */
@Data
@Table(name = "technician_certificate_images")
public class TechnicianCertificateImages extends BaseModelInfo {

    /**
     * 技师uuid
     */
    @Column(name = "technician_uuid")
    private String technicianUuid;

    /**
     * 证书图片类型:0 国家等级鉴定 1 主机厂认证
     */
    @Column(name = "img_type")
    private Integer imgType;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

}
