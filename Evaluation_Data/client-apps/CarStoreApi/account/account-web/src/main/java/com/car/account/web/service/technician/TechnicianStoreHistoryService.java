package com.car.account.web.service.technician;

import com.car.account.client.response.storehistory.QueryStoreHistoryRes;
import com.car.common.res.ResultRes;

import java.util.List;

/**
 * @author xlj
 */
public interface TechnicianStoreHistoryService {

    /**
     * 查询技师关联店铺历史记录信息
     * @param technicianUuid
     * @return
     */
    ResultRes<List<QueryStoreHistoryRes>> queryStoreHistory(String technicianUuid);

    /**
     * 查询技师最近关联店铺
     * @param technicianUuid
     * @return
     */
    ResultRes<QueryStoreHistoryRes> queryLastStoreHistory(String technicianUuid);
}
