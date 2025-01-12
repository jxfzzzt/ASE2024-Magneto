package com.car.order.web.model.dtc;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import org.springframework.cloud.sleuth.instrument.web.ClientSampler;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2021/2/17
 */
@Data
@Table(name = "dtc")
public class Dtc extends BaseModelInfo {


    /**
     * 发布人uuid
     */
    @Column(name = "dtc_issuer_uuid")
    private String dtcIssuerUuid;

    /**
     * dtc故障代码
     */
    @Column(name = "dtc_code")
    private String dtcCode;

    /**
     * dtc标题
     */
    @Column(name = "dtc_definition")
    private String dtcDefinition;

    /**
     * dtc类型，对应字典表
     */
    @Column(name = "dtc_type")
    private String dtcType;

    /**
     * dtc发布关联品牌(对应车辆品牌uuid)
     */
    @Column(name = "dtc_brand_uuid")
    private String dtcBrandUuid;

    /**
     * dtc购买金额
     */
    @Column(name = "dtc_amount")
    private BigDecimal dtcAmount;

    /**
     * 发布人类型：0：后台发布，1：技师 ，2：店铺
     */
    @Column(name = "dtc_issuer_type")
    private Integer dtcIssuerType;

    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "dtc_check_sts")
    private Integer dtcCheckSts;

    /**
     * 具体内容
     */
    @Column(name = "dtc_remarks")
    private String dtcRemarks;
}
