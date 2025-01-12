package com.car.order.web.model.instance;

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
@Table(name = "order_case_detail")
public class OrderCaseDetail extends BaseModelInfo {

    /**
     * 订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 案例uuid
     */
    @Column(name = "case_uuid")
    private String caseUuid;

    /**
     * 案例名称
     */
    @Column(name = "case_name")
    private String caseName;

    /**
     * 案例数量
     */
    @Column(name = "case_num")
    private Integer caseNum;

    /**
     * 案例资源地址
     */
    @Column(name = "case_img_url")
    private String caseImgUrl;

    /**
     * 案例价格
     */
    @Column(name = "materials_expenses")
    private BigDecimal materialsExpenses;
}
