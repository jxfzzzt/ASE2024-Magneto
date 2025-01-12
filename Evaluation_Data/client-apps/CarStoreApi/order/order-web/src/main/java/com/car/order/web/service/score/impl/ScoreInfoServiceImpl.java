package com.car.order.web.service.score.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.request.order.score.AddScoreInfoReq;
import com.car.order.client.request.order.score.QueryScoreListReq;
import com.car.order.client.response.order.score.QueryScoreInfoListRes;
import com.car.order.client.response.order.score.QueryScoreInfoRes;
import com.car.order.web.mapper.score.ScoreInfoMapper;
import com.car.order.web.model.score.ScoreInfo;
import com.car.order.web.service.score.ScoreInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Slf4j
@Service
public class ScoreInfoServiceImpl implements ScoreInfoService {

    @Autowired
    private ScoreInfoMapper scoreInfoMapper;

    /**
     * 新增评分信息
     * @param req
     * @return
     */
    @Override
    public ResultRes<String> addScore(AddScoreInfoReq req) {
        ScoreInfo scoreInfo = new ScoreInfo();
        scoreInfo.setUuid(UuidUtils.getUuid());
        scoreInfo.setOrderUuid(req.getOrderUuid());
        scoreInfo.setScoreType(req.getScoreType());
        scoreInfo.setScoreStar(req.getScoreStar());
        scoreInfo.setUserUuid(String.valueOf(TokenHelper.getUserUuid()));
        scoreInfo.setSts(StsEnum.ACTIVE.getValue());
        scoreInfo.setCreatedBy(TokenHelper.getUserName());
        scoreInfo.setCreatedTime(new Date());
        scoreInfo.setRelationUuid(req.getRelationUuid());
        int addScoreInfoNum = scoreInfoMapper.insert(scoreInfo);
        if (addScoreInfoNum <= 0 ) {
            log.error("新增评分失败，新增参数为：{}", JSON.toJSONString(scoreInfo));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return ResultRes.success(scoreInfo.getUuid());
    }

    /**
     * 查询评分信息列表
     * @param req
     * @return
     */
    @Override
    public PageRes<List<QueryScoreInfoListRes>> queryScoreList(QueryScoreListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        List<QueryScoreInfoListRes> resList = scoreInfoMapper.queryScoreList(req, userUuid);
        PageInfo<QueryScoreInfoListRes> pageInfo = new PageInfo<>(resList);
        return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询评分信息详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<QueryScoreInfoRes> queryScoreInfo(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //查询评分详情
        ScoreInfo scoreInfo = scoreInfoMapper.selectByPrimaryKey(uuid);
        QueryScoreInfoRes res = new QueryScoreInfoRes();
        if (null != scoreInfo) {
            BeanUtils.copyProperties(scoreInfo, res);
        }
        return ResultRes.success(res);
    }

    /**
     * 删除评分信息
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<String> deleteScore(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        ScoreInfo scoreInfo = new ScoreInfo();
        scoreInfo.setUuid(uuid);
        scoreInfo.setSts(StsEnum.INVALID.getValue());
        scoreInfo.setLastUpdatedBy(TokenHelper.getUserName());
        scoreInfo.setLastUpdatedTime(new Date());
        int deleteNum = scoreInfoMapper.updateByPrimaryKeySelective(scoreInfo);
        if (deleteNum <= 0) {
            log.error("删除评分信息失败，请求参数为：{}", JSON.toJSONString(scoreInfo));
            throw new BusinessException(ResEnum.DELETE_DB_ERROR);
        }
        return ResultRes.success(uuid);
    }

    /**
     * 查询商品/店铺/服务评分
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<String> queryGoodsScore(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException(ResEnum.DELETE_DB_ERROR);
        }
        String goodsScore = scoreInfoMapper.queryGoodsScore(uuid);
        return ResultRes.success(goodsScore);
    }
}
