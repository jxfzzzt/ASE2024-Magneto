package com.car.order.web.mapper.score;

import com.car.common.res.ResultRes;
import com.car.order.client.request.order.score.QueryScoreListReq;
import com.car.order.client.response.order.score.QueryScoreInfoListRes;
import com.car.order.web.model.score.ScoreInfo;
import com.car.order.web.model.technician.cases.TechnicianCaseImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Repository
public interface ScoreInfoMapper extends Mapper<ScoreInfo> {

    /**
     * 查询评分信息列表
     * @param req
     * @param userUuid
     * @return
     */
    List<QueryScoreInfoListRes> queryScoreList(@Param("req") QueryScoreListReq req, @Param("userUuid") String userUuid);

    /**
     * 查询评分
     * @param orderUuid
     * @param scoreType
     * @return
     */
    BigDecimal queryScore(@Param("orderUuid") String orderUuid, @Param("scoreType") Integer scoreType);

    /**
     * 查询商品评分
     * @param uuid
     * @return
     */
    String queryGoodsScore(@Param("uuid") String uuid);
}
