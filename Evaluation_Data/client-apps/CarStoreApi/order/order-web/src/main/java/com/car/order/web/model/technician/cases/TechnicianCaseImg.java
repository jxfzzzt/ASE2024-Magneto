package com.car.order.web.model.technician.cases;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 
 *
 * @author zhangyp
 * @createDate 2021-01-21
 */
@Data
@Table(name = "technician_case_img")
@EqualsAndHashCode(callSuper = true)
public class TechnicianCaseImg extends BaseModelInfo {
    /**
     * 技师案例uuid
     */
    @Column(name = "case_uuid")
    private String caseUuid;

    /**
     * 文件/图片url
     */
    @Column(name = "url")
    private String url;
}