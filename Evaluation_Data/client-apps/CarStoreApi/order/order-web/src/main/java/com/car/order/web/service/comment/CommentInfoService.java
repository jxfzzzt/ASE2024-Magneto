package com.car.order.web.service.comment;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.comment.AddCommentReq;
import com.car.order.client.request.comment.QueryCommentInfoListReq;
import com.car.order.client.response.comment.CommentInfoListRes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
public interface CommentInfoService {


    /**
     * 新增评论信息
     * @param req
     * @return
     */
    ResultRes<String> addComment(AddCommentReq req);

    /**
     * 查询评论列表信息
     * @param req
     * @return
     */
    PageRes<List<CommentInfoListRes>> queryCommentList(QueryCommentInfoListReq req);


    /**
     * 删除评论信息
     * @param uuid
     * @return
     */
    ResultRes<String> deleteComment(String uuid);

    /**
     * 查询商品评价数量
     * @param uuid
     * @return
     */
    ResultRes<Integer> queryGoodsCommentCount(String uuid);
}
