package com.car.account.web.mapper.technician;

import com.car.account.client.response.storehistory.QueryStoreHistoryRes;
import com.car.account.web.model.technician.TechnicianStoreHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * @author xlj
 */
@Repository
public interface TechnicianStoreHistoryMapper extends Mapper<TechnicianStoreHistory> {

    /**
     * 查询技师关联店铺历史记录信息
     * @param technicianUuid
     * @return
     */
    List<QueryStoreHistoryRes> queryStoreHistory(@Param("technicianUuid") String technicianUuid);
}
