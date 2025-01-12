package com.car.order.web.model.dtc;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2021/2/17
 */
@Data
@Table(name = "dtc_content")
public class DtcContent extends BaseModelInfo {

    /**
     * dtc故障uuid
     */
    @Column(name = "dtc_uuid")
    private String dtcUuid;

    /**
     * dtc故障说明(富文本图文)
     */
    @Column(name = "dtc_explain")
    private String dtcExplain;

    /**
     * dtc故障可能原因(富文本图文)
     */
    @Column(name = "dtc_reasons")
    private String dtcReasons;

    /**
     * dtc故障诊断辅助(富文本图文)
     */
    @Column(name = "dtc_diagnose")
    private String dtcDiagnose;
}
