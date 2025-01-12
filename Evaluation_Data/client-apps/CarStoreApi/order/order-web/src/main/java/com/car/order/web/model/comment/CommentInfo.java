package com.car.order.web.model.comment;

import com.car.common.datasource.model.BaseModelInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author cjw
 * @date 2021/1/2
 */
@Data
@Table(name = "comment_info")
public class CommentInfo extends BaseModelInfo {

    /**
     * 订单号
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 评论人
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 评论详情
     */
    @Column(name = "comment_desc")
    private String commentDesc;

    /**
     * 备注/驳回原因
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @Column(name = "check_sts")
    private Integer checkSts;

    /**
     * 商品/店铺uuid
     */
    @Column(name = "relation_uuid")
    private String relationUuid;

    /**
     * 评论人名称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 评论类型1商品2服务3店铺
     */
    @Column(name = "score_type")
    private Integer scoreType;
}
