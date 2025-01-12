package com.car.order.web.model.score;

import com.car.common.datasource.model.BaseModelInfo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Data
@Table(name = "score_info")
public class ScoreInfo extends BaseModelInfo {

    /**
     * 订单唯一标识
     */
    @Column(name = "order_uuid")
    private String orderUuid;

    /**
     * 打分类型1商品2服务3店铺
     */
    @Column(name = "score_type")
    private Integer scoreType;

    /**
     * 星值eg:4.5
     */
    @Column(name = "score_star")
    private BigDecimal scoreStar;

    /**
     * 评分用户
     */
    @Column(name = "user_uuid")
    private String userUuid;

    /**
     * 商品/服务/店铺uuid
     */
    @Column(name = "relation_uuid")
    private String relationUuid;
}
