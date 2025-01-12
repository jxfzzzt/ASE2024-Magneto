package com.car.order.web.service.content;

import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.order.client.request.content.AddContentReq;
import com.car.order.client.request.content.CheckContentReq;
import com.car.order.client.request.content.QueryContentListReq;
import com.car.order.client.response.content.ContentDetailRes;
import com.car.order.client.response.content.QueryContentListRes;
import com.car.order.client.response.order.consult.CaseDetails;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
public interface ContentService {

    /**
     * 添加内容
     * @param addContentReq
     * @return
     */
    ResultRes<String> addContent(AddContentReq addContentReq);

    /**
     * 删除内容
     * @param uuid
     * @return
     */
    ResultRes<String> deleteContent(String uuid);

    /**
     * 审核内容
     * @param checkContentReq
     * @return
     */
    ResultRes<String> checkContent(CheckContentReq checkContentReq);

    /**
     * 查询内容列表
     * @param param
     * @return
     */
    PageRes<List<QueryContentListRes>> queryContentList(QueryContentListReq param);

    /**
     * 查询内容详情
     * @param uuid
     * @return
     */
    ResultRes<ContentDetailRes> queryContentDetail(String uuid);

    /**
     * 案例详情
     * @param uuid
     * @return
     */
    ResultRes<CaseDetails> getCaseDetails(String uuid);


    /**
     * 我得案例
     * @param uuid
     * @return
     */
    ResultRes<List<CaseDetails>> getMyCaseList(String uuid,Integer type);

}
