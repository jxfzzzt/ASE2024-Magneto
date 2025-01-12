package com.car.order.web.service.comment.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.CheckStatusEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.order.client.enums.goods.EvaluateStsEnum;
import com.car.order.client.enums.score.ScoreTypeEnum;
import com.car.order.client.request.comment.AddCommentReq;
import com.car.order.client.request.comment.QueryCommentInfoListReq;
import com.car.order.client.response.comment.CommentInfoListRes;
import com.car.order.web.mapper.comment.CommentInfoMapper;
import com.car.order.web.mapper.content.ContentMapper;
import com.car.order.web.mapper.goods.OrderGoodsMapper;
import com.car.order.web.model.comment.CommentInfo;
import com.car.order.web.model.content.Content;
import com.car.order.web.model.goods.OrderGoods;
import com.car.order.web.service.comment.CommentInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Slf4j
@Service
public class CommentInfoServiceImpl implements CommentInfoService {


    @Autowired
    private CommentInfoMapper commentInfoMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    /**
     * 新增评论信息
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultRes<String> addComment(AddCommentReq req) {
        //用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //用户名称
        String userName = TokenHelper.getUserName();
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setUuid(UuidUtils.getUuid());
        commentInfo.setOrderUuid(req.getOrderUuid());
        commentInfo.setUserUuid(userUuid);
        commentInfo.setUserName(userName);
        commentInfo.setCommentDesc(req.getCommentDesc());
        commentInfo.setCheckSts(CheckStatusEnum.CHECK_PENDING.getValue());
        commentInfo.setCreatedBy(userName);
        commentInfo.setRelationUuid(req.getRelationUuid());
        commentInfo.setSts(StsEnum.ACTIVE.getValue());
        commentInfo.setCreatedTime(new Date());
        commentInfo.setScoreType(req.getScoreType());
        //新增评论信息
        int addCommentInfoNum = commentInfoMapper.insert(commentInfo);
        if (addCommentInfoNum <= 0) {
            log.error("新增评论信息失败，新增参数>>>{}", JSON.toJSONString(commentInfo));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        Content content = new Content();
        content.setUuid(UuidUtils.getUuid());
        content.setOrderUuid(req.getOrderUuid());
        content.setUserUuid(userUuid);
        content.setMobile(req.getMobile());
        content.setCheckSts(CheckStatusEnum.CHECK_PENDING.getValue());
        content.setContentDetail(req.getCommentDesc());
        content.setSts(StsEnum.ACTIVE.getValue());
        content.setCreatedTime(new Date());
        content.setCreatedBy(userName);
        //新增审核信息
        int addContentNum = contentMapper.insert(content);
        if (addContentNum <= 0) {
            log.error("新增审核信息失败，新增参数>>>{}", JSON.toJSONString(content));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        if (ScoreTypeEnum.GOODS.getValue().equals(req.getScoreType())) {
            //修改商品订单评论状态
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setUuid(req.getOrderUuid());
            orderGoods.setEvaluateSts(EvaluateStsEnum.COMMENTED.getValue());
            orderGoods.setLastUpdatedBy(userName);
            orderGoods.setLastUpdatedTime(new Date());
            int updateNum = orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
            if (updateNum <= 0 ) {
                log.error("修改维修订单评论状态失败，请求参数：{}", orderGoods);
                throw new BusinessException(ResEnum.UPDATE_DB_ERROR.getValue());
            }
        }
        return ResultRes.success(commentInfo.getUuid());
    }

    /**
     * 查询评论列表信息
     * @param req
     * @return
     */
    @Override
    public PageRes<List<CommentInfoListRes>> queryCommentList(QueryCommentInfoListReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        //查询评论列表信息
        List<CommentInfoListRes> resList = commentInfoMapper.queryCommentList(req);
        PageInfo<CommentInfoListRes> pageInfo = new PageInfo<>(resList);
        return PageRes.success(resList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 删除评论信息
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultRes<String> deleteComment(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            log.error("评论uuid不能为空");
            throw new BusinessException(ResEnum.LACK_PARAMETER.getValue());
        }
        CommentInfo deleteCommentInfo = new CommentInfo();
        deleteCommentInfo.setUuid(uuid);
        deleteCommentInfo.setSts(StsEnum.INVALID.getValue());
        int deleteCommentInfoNum = commentInfoMapper.updateByPrimaryKeySelective(deleteCommentInfo);
        if (deleteCommentInfoNum <= 0) {
            log.error("删除订单信息失败，请求参数为>>>{}", JSON.toJSONString(deleteCommentInfo));
            throw new BusinessException(ResEnum.DELETE_DB_ERROR);
        }
        return ResultRes.success(uuid);
    }

    /**
     * 查询商品评价数量
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<Integer> queryGoodsCommentCount(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        Integer count = commentInfoMapper.queryGoodsCommentCount(uuid);
        return ResultRes.success(count);
    }
}
