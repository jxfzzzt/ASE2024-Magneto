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
@Table(name = "consult_detail")
public class ConsultDetail extends BaseModelInfo {

    /**
     * 咨询uuid
     */
    @Column(name = "consult_uuid")
    private String consultUuid;

    /**
     * 咨询描述
     */
    @Column(name = "consult_desc")
    private String consultDesc;

    /**
     * 回答描述
     */
    @Column(name = "answer_desc")
    private String answerDesc;

}
