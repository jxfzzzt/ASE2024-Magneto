package com.car.order.web.mapper.dtc;

import com.car.order.client.request.dtc.AddDtcReq;
import com.car.order.client.response.dtc.QueryDtcInfoRes;
import com.car.order.web.model.dtc.DtcContent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhouz
 * @date 2021/2/17
 */
@Repository
public interface DtcContentMapper extends Mapper<DtcContent> {

    /**
     * 修改dtc详情
     * @param req
     * @param uuid
     * @param userName
     * @return
     */
    int updateDtcContentInfo(@Param("req") AddDtcReq req, @Param("uuid") String uuid, @Param("userName") String userName);

    /**
     * 删除dtc详情信息
     * @param uuid
     * @param userName
     * @return
     */
    int deleteDtcContentInfo(@Param("uuid") String uuid, @Param("userName") String userName);

    /**
     * 根据dtcUuid查询dtc详情信息
     * @param dtcUuid
     * @return
     */
    DtcContent queryDtcUuidInfo(@Param("dtcUuid") String dtcUuid);

}
