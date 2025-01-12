package com.car.order.web.service.score;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.order.score.AddScoreInfoReq;
import com.car.order.client.request.order.score.QueryScoreListReq;
import com.car.order.client.response.order.score.QueryScoreInfoListRes;
import com.car.order.client.response.order.score.QueryScoreInfoRes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
public interface ScoreInfoService {

    /**
     * 新增评分信息
     * @param req
     * @return
     */
    ResultRes<String> addScore(AddScoreInfoReq req);


    /**
     * 查询评分列表信息
     * @param req
     * @return
     */
    PageRes<List<QueryScoreInfoListRes>> queryScoreList(QueryScoreListReq req);

    /**
     * 查询评分信息详情
     * @param uuid
     * @return
     */
    ResultRes<QueryScoreInfoRes> queryScoreInfo(String uuid);

    /**
     * 删除评分信息
     * @param uuid
     * @return
     */
    ResultRes<String> deleteScore(String uuid);

    /**
     * 查询商品/店铺/服务评分
     * @param uuid
     * @return
     */
    ResultRes<String> queryGoodsScore(String uuid);
}
