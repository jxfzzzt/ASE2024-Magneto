package com.car.order.web.model.dtc;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
@Table(name = "dtc_order_detail")
public class DtcOrderDetail  extends BaseModelInfo {

    /**
     * dtc订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * dtc故障代码(新增手动输入)
     */
    @Column(name = "dtc_code")
    private String dtcCode;

    /**
     * dtc发布关联品牌(对应车辆品牌uuid)
     */
    @Column(name = "dtc_brand_uuid")
    private String dtcBrandUuid;

    /**
     * dtc定义(标题)
     */
    @Column(name = "dtc_definition")
    private String dtcDefinition;

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
