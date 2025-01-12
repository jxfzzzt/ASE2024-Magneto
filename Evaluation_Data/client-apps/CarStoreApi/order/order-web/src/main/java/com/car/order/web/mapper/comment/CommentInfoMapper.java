package com.car.order.web.mapper.comment;

import com.car.common.res.ResultRes;
import com.car.order.client.request.comment.QueryCommentInfoListReq;
import com.car.order.client.response.comment.CommentInfoListRes;
import com.car.order.web.model.comment.CommentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Repository
public interface CommentInfoMapper extends Mapper<CommentInfo> {


    /**
     * 查询评论列表信息
     * @param req
     * @return
     */
    List<CommentInfoListRes> queryCommentList(@Param("req") QueryCommentInfoListReq req);


    /**
     * 查询商品评价数量
     * @param uuid
     * @return
     */
    Integer queryGoodsCommentCount(@Param("uuid") String uuid);
}
