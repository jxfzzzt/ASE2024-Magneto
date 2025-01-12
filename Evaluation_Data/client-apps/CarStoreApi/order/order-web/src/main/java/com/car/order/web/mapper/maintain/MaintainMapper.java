package com.car.order.web.mapper.maintain;

import com.car.order.client.request.maintain.QueryMaintainListReq;
import com.car.order.client.response.maintain.QueryMaintainRes;
import com.car.order.web.model.maintain.Maintain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 19:02
 */
@Repository
public interface MaintainMapper extends Mapper<Maintain> {

    /**
     * 查询dtc列表
     *
     * @return
     */
    List<QueryMaintainRes> queryMaintainList(@Param("req") QueryMaintainListReq req);

    /**
     * 根据uuid查询
     * @param uuid
     * @return
     */
    QueryMaintainRes  getById(@Param("uuid") String uuid);

    /**
     * 删除
     * @param uuid
     * @param userName
     * @return
     */
    int deleteMaintainInfo(@Param("uuid") String uuid, @Param("userName") String userName);

}
