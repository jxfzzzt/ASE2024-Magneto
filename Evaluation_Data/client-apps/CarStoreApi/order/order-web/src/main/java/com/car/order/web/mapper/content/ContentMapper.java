package com.car.order.web.mapper.content;

import com.car.order.client.request.content.QueryContentListReq;
import com.car.order.client.response.content.ContentDetailRes;
import com.car.order.client.response.content.QueryContentListRes;
import com.car.order.client.response.order.consult.CaseDetails;
import com.car.order.web.model.content.Content;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Repository
public interface ContentMapper extends Mapper<Content> {

    /**
     * 查询内容列表
     * @param param
     * @return
     */
    List<QueryContentListRes> queryContentList(QueryContentListReq param);

    /**
     * 查询内容详情
     * @param uuid
     * @return
     */
    ContentDetailRes queryContentDetail(String uuid);


    /**
     * 根据咨询id与图片类型查询图片信息
     * @param consultUuid
     * @param imgType
     * @return
     */
    List<String> queryImgUrl(@Param("consultUuid") String consultUuid, @Param("imgType") Integer imgType);


    /**
     * 案例详情
     * @param uuid
     * @return
     */
    CaseDetails getCaseDetails(String uuid);

    /**
     * 我发布的案例
     * @param uuid
     */
    List<CaseDetails> getMyCaseList(@Param("uuid")String uuid);

    /**
     * 我购买的案例
     * @return
     */
    List<CaseDetails> getPurchaseCase(@Param("uuid")String uuid);

}
