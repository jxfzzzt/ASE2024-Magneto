package com.car.order.web.model.maintain;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 18:52
 */
@Data
@Table(name = "maintain")
public class Maintain extends BaseModelInfo {


    /**
     * 养护标题
     */
    @Column(name = "maintain_title")
    private String maintainTitle;

    /**
     * 养护封面
     */
    @Column(name = "maintain_cover")
    private String maintainCover;

    /**
     * 养护内容
     */
    @Column(name = "maintain_content")
    private String maintainContent;


    /**
     * 品牌uuid
     */
    @Column(name = "brand_uuid")
    private String brandUuid;

    /**
     * 车型uuid
     */
    @Column(name = "car_model_uuid")
    private String carModelUuid;

    /**
     * 所属系统字典表attach_sys
     */
    @Column(name = "attach_sys")
    private String attachSys;

    /**
     * 发布者uuid
     */
    @Column(name = "issuer_uuid")
    private String issuerUuid;
    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "maintain_check_sts")
    private Integer maintainCheckSts;

    /**
     * 具体内容
     */
    @Column(name = "maintain_remarks")
    private String maintainRemarks;
}