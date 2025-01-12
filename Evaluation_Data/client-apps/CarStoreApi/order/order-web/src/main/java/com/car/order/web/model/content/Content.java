package com.car.order.web.model.content;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Data
@Table(name = "content")
public class Content extends BaseModelInfo {
    /**
     * 订单uuid
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 订单名称
     */
    @Column(name = "order_name")
    private String orderName;

    /**
     * 订单分类:0 订单点评  1 提问 2 回答 3 案例
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 用户uuid
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 具体内容
     */
    @Column(name = "content_detail")
    private String contentDetail;

}
