package com.car.order.web.mapper.dtc;

import com.car.order.client.request.dtc.QueryDtcListReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.client.response.dtc.QueryDtcListRes;
import com.car.order.web.model.dtc.Dtc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2021/2/17
 */
@Repository
public interface DtcMapper extends Mapper<Dtc> {

    /**
     * 根据id查询详情
     * @param uuid
     * @return
     */
    QueryDtcInfoRes getById(@Param("uuid") String uuid);

    /**
     * 删除dtc信息
     * @param uuid
     * @param userName
     * @return
     */
    int deleteDtcInfo(@Param("uuid") String uuid, @Param("userName") String userName);

    /**
     * 查询dtc列表
     * @param req
     * @return
     */
    List<QueryDtcListRes> queryDtcList(@Param("req") QueryDtcListReq req);
}
